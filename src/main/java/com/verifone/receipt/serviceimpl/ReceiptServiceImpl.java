/**********************************************************************
 * The ReceiptServiceImpl class is related to get the receipt          *
 * related Service operation.                                          *
 *                                                                     *
 * @version 1.0                                                        *
 * @since 06/04/2019                                                   *
 **********************************************************************/
package com.verifone.receipt.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.verifone.api.receipt.model.ReceiptTransaction;
import com.verifone.api.receipt.model.SendReceipt;
import com.verifone.receipt.exception.*;
import com.verifone.receipt.model.Mail;
import com.verifone.receipt.model.Template;
import com.verifone.receipt.service.IEmailService;
import com.verifone.receipt.service.IHtmlFormattingService;
import com.verifone.receipt.service.IReceiptService;
import com.verifone.receipt.validation.ReceiptServiceValidation;
import com.verifone.receipt.validation.SizeLimitValidation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.verifone.receipt.constant.ReceiptConstants.*;

@Service
public class ReceiptServiceImpl implements IReceiptService {

    private static final Logger LOG = LoggerFactory.getLogger(ReceiptServiceImpl.class);

    @Autowired
    public IEmailService emailService;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Value("${receipt.file.location}")
    private String fileLocation;

    @Value("${receipt.email.attachment}")
    private String emailAttachment;

    @Value("${receipt.storeReceipt.enable}")
    private String storeReceipt;

    @Autowired
    private IHtmlFormattingService htmlFormattingService;

    @Autowired(required = true)
    private ReceiptServiceValidation receiptServiceValidation;

    @Override
    public void processReceipt(ReceiptTransaction receiptTransaction)
            throws InvalidTemplateDataException, EmailServiceException, InvalidTemplateException {
        LOG.info("ReceiptServiceImpl processReceipt method called");
        receiptServiceValidation.validateRequireField(receiptTransaction, null, true);
        receiptServiceValidation.validateFields(receiptTransaction, null, true);
        validateTransactionType(receiptTransaction);
        final Long startTime = System.currentTimeMillis();
        CompletableFuture.runAsync(() -> {
            try {
                Mail mail = new Mail();

                mail.setFrom(receiptTransaction.getFromName());
                mail.setTo(receiptTransaction.getTo());
                mail.setSubject(receiptTransaction.getSubject());

                htmlFormattingService.formatReceipt(receiptTransaction, mail);

                if (Objects.isNull(mail.getSubject()))
                    throw new InvalidTemplateDataException("Subject field not available in request and template");

                emailService.sendEmail(mail);
                File file1 = new File(fileLocation + TEMPLATES + mail.getImageName());
                boolean isFileDeleted = file1.delete();
                LOG.info("Image File deleted : {} ", isFileDeleted);
                if ("yes".equalsIgnoreCase(storeReceipt)) {
                    String receiptName = receiptTransaction.getMerchant().getMerchantId() + receiptTransaction.getTransaction().getInitiatorTraceId() + ".html";
                    File file = new File(fileLocation + TEMPLATES + receiptName);
                    FileWriter output = new FileWriter(file);
                    output.write(mail.getContent());
                    output.close();
                    storeReceiptInDB(file, RECEIPT_FILE_TYPE, receiptTransaction, mail);
                    File file2 = new File(fileLocation + TEMPLATES + receiptName);
                    boolean receiptDeleted = file2.delete();
                    LOG.info("Receipt File deleted : {}", receiptDeleted);
                }
                LOG.info("Total time taken SendingEmail in ms : {} ", (System.currentTimeMillis() - startTime));
            } catch (Exception e) {
                LOG.error("Error while sending mail");
            }
        });

        LOG.info("ReceiptServiceImpl processReceipt method ends");
    }

    private void storeReceiptInDB(File file, String fileType, ReceiptTransaction receiptTransaction, Mail mail) throws IOException {
        LOG.info("Processing Receipt into the mongo DB");
        DBObject metaData = new BasicDBObject();

        receiptTransaction.setSubject(mail.getSubject());
        ObjectMapper mapper = new ObjectMapper();
        String rJson = mapper.writeValueAsString(receiptTransaction);
        JsonObject jsonObject = new Gson().fromJson(rJson, JsonObject.class);
        Gson gson = new GsonBuilder().create();
        rJson = gson.toJson(jsonObject);
        try {
            if (RECEIPT_FILE_TYPE.equalsIgnoreCase(fileType)) {
                metaData.put("type", "file");
                metaData.put("merchantId", receiptTransaction.getMerchant().getMerchantId());
                metaData.put("poiId", receiptTransaction.getMerchant().getPoiId());
                metaData.put("stan", receiptTransaction.getTransaction().getInitiatorTraceId());
                metaData.put("e-mail", mail.getTo());
                metaData.put("subject", mail.getSubject());
                metaData.put(FILE_TYPE_KEY, fileType);
                metaData.put("jsonRequest", rJson);
            } else {
                metaData.put("type", "file");
                metaData.put(FILE_TYPE_KEY, fileType);
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile mf = new MockMultipartFile(file.getName(), fileInputStream);
            gridFsOperations.store(mf.getInputStream(), mf.getName(), mf.getContentType(), metaData);
        } catch (FileNotFoundException e) {
            LOG.error("Error while storing template ", e);
        }
    }


    @Override
    public void sendHTMLReceipt(SendReceipt sendReceipt)
            throws InvalidTemplateDataException, EmailServiceException, InvalidTemplateException {
        LOG.info("ReceiptServiceImpl sendHTMLReceipt method called");
        receiptServiceValidation.validateRequireField(null, sendReceipt, false);
        receiptServiceValidation.validateFields(null, sendReceipt, false);
        final Long startTime = System.currentTimeMillis();
        CompletableFuture.runAsync(() -> {
            try {
                Mail mail = new Mail();
                mail.setFrom(sendReceipt.getFromName());
                mail.setTo(sendReceipt.getTo());
                mail.setSubject(sendReceipt.getSubject());

                htmlFormattingService.formatPreformatted(sendReceipt, mail);
                if (Objects.isNull(mail.getSubject()))
                    throw new InvalidTemplateDataException("Subject field not available in request and template ");

                emailService.sendEmailSimple(mail);
                File file1 = new File(fileLocation + TEMPLATES + "output.pdf");
                File file2 = new File(fileLocation + TEMPLATES + "preformat.html");
                File file3 = new File(fileLocation + TEMPLATES + "output.png");
                boolean isFileDeleted = file1.delete();
                boolean isFileDeleted2 = file2.delete();
                boolean isFileDeleted3 = file3.delete();
                LOG.info("pdf File deleted: {}", isFileDeleted + " html file deleted : " + isFileDeleted2 + " png file deleted :" + isFileDeleted3);
                LOG.info("Total time taken SendingEmail in ms : {} ", (System.currentTimeMillis() - startTime));
            } catch (Exception e) {
                LOG.error("Error while sending mail");
            }
        });

        LOG.info("ReceiptServiceImpl sendHTMLReceipt method ends");
    }


    @Override
    public String saveTemplate(String templateString) throws EmailServiceException {
        LOG.info("ReceiptServiceImpl saveTemplate method called");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(templateString.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String fileName = no.toString(SIXTEEN);
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("templates").getFile() + SLASH + fileName + ".ftl");
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(templateString);
            }
            LOG.info("ReceiptServiceImpl saveTemplate method ends");
            return fileName;
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new EmailServiceException("Error Saving Template String");
        }
    }

    private void validateTransactionType(ReceiptTransaction receiptTransaction) throws InvalidTemplateDataException {
        LOG.info("ReceiptServiceImpl validateTransactionType method called");
        try {
            File file = new File(fileLocation + TEMPLATES + receiptTransaction.getTemplateId() + ".ftl");
            Document doc = Jsoup.parse(file, "UTF-8");
            Elements metaTags = doc.getElementsByTag("meta");

            for (Element metaTag : metaTags) {
                String field = metaTag.attr("name");
                String value = metaTag.attr("content");

                if ("transactionType".equalsIgnoreCase(field) && (!value.contains(receiptTransaction.getTransaction().getTransactionType().toString()))) {
                    throw new InvalidTemplateDataException(
                            "TransactionType " + receiptTransaction.getTransaction().getTransactionType().toString() + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
                }
            }
        } catch (Exception e) {
            throw new InvalidTemplateDataException("Invalid data or template", e);
        }
    }

    @Override
    public String addTemplate(MultipartFile file) {
        DBObject metadata = new BasicDBObject();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (!(fileName.contains(".ftl"))) {
            throw new FileStorageException("Invalid type of the File");
        }
        try {
            metadata.put("type", "data");
            metadata.put(FILE_TYPE_KEY, TEMPLATE_FILE_TYPE);
            GridFSFile fsFile = gridFsTemplate.findOne(new Query(Criteria.where(FILE_NAME).is(fileName)));
            if (fsFile == null) {
                gridFsOperations.store(file.getInputStream(), fileName, CONTENT_TYPE, metadata);
                storingTemplates(fileName);
            } else {
                throw new FileStorageException("File already exist in DB");
            }
        } catch (IOException io) {
            LOG.error("Error while adding Template ", io);
        }
        return "Success";
    }

    private void storingTemplates(String fileName) {
        Template tem;
        File file1 = new File(fileLocation + TEMPLATES + fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file1)) {
            tem = new Template();
            tem.setInputStream(gridFsOperations.getResource(fileName).getInputStream());
            FileCopyUtils.copy(tem.getInputStream(), fileOutputStream);
        } catch (IOException io) {
            LOG.error("Error while storing templates ", io);
        }
    }

    @Override
    public Template retrieveTemplate(String filename) throws IOException {
        if (!filename.contains(".ftl")) {
            filename = filename + ".ftl";
        }
        GridFSFile fsFile = gridFsTemplate.findOne(new Query(Criteria.where(FILE_NAME).is(filename)));
        Template tem = null;
        if (fsFile != null) {
            File file = new File(fileLocation + TEMPLATES + fsFile.getFilename());
            InputStream inputStream = gridFsOperations.getResource(fsFile).getInputStream();
            String fileContent = convertInputStreamToString(inputStream);
            try (FileWriter output = new FileWriter(file)) {
                output.write(fileContent);
            }
            tem = new Template();
            tem.setInputStream(gridFsOperations.getResource(fsFile).getInputStream());
        } else {
            throw new FileStorageException(FILE_NOT_EXIST);
        }
        return tem;
    }

    @Override
    public String updateTemplate(MultipartFile file, String fileName) {
        DBObject metadata = new BasicDBObject();
        if (!fileName.contains(".ftl")) {
            fileName = fileName + ".ftl";
        }
        try {
            metadata.put("type", "data");
            metadata.put(FILE_TYPE_KEY, TEMPLATE_FILE_TYPE);
            GridFSFile fsFile = gridFsTemplate.findOne(new Query(Criteria.where(FILE_NAME).is(fileName)));
            if (fsFile == null) {
                gridFsOperations.store(file.getInputStream(), fileName, CONTENT_TYPE, metadata);
                storingTemplates(fileName);
            } else {
                //delete old file first
                gridFsOperations.delete(new Query(Criteria.where(FILE_NAME).is(fileName)));
                //after deleting add new file
                gridFsOperations.store(file.getInputStream(), fileName, CONTENT_TYPE, metadata);
            }
        } catch (IOException io) {
            LOG.error("Error while updating template ", io);
        }
        return "updated file";
    }

    @Override
    public String deleteTemplate(String fileName) {
        if (!fileName.contains(".ftl")) {
            fileName = fileName + ".ftl";
        }
        GridFSFile fsFile = gridFsTemplate.findOne(new Query(Criteria.where(FILE_NAME).is(fileName)));
        if (fsFile != null) {
            gridFsOperations.delete(new Query(Criteria.where("filename").is(fileName)));
            File file1 = new File(fileLocation + TEMPLATES + fsFile.getFilename());
            boolean templateFile = file1.delete();
            LOG.info("Deleted template in default config : {} ", templateFile);
        } else {
            throw new FileStorageException(FILE_NOT_EXIST);
        }
        return "deleted";
    }

    @Override
    public void retrieveAllTemplate() throws IOException {
        List<GridFSFile> fsFiles = new ArrayList<>();
        gridFsTemplate.find(new Query(Criteria.where("metadata.fileType").is(TEMPLATE_FILE_TYPE))).into(fsFiles);
        Template tem = null;
        if (!(fsFiles.isEmpty())) {
            for (GridFSFile fs : fsFiles) {
                File file = new File(fileLocation + TEMPLATES + fs.getFilename());
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    tem = new Template();
                    tem.setInputStream(gridFsOperations.getResource(fs).getInputStream());
                    FileCopyUtils.copy(tem.getInputStream(), fileOutputStream);
                } catch (IOException io) {
                    LOG.error("Error while retrieving template ", io);
                }
            }
        } else {
            throw new FileStorageException(FILE_NOT_EXIST);
        }
    }

    @Override
    public void reprintReceipt(ReceiptTransaction receiptTransaction) throws EmailServiceException {
        LOG.info("Reprint Receipt Function got invoked");
        try {
            GridFSFile fsFile = gridFsTemplate.findOne(new Query(Criteria.where("metadata.merchantId").is(receiptTransaction.getMerchant().getMerchantId()).and("metadata.stan").is(receiptTransaction.getTransaction().getInitiatorTraceId())));
            if (fsFile != null) {
                String email = fsFile.getMetadata().getString("e-mail");
                String subject = fsFile.getMetadata().getString("subject");
                InputStream inputStream = gridFsOperations.getResource(fsFile).getInputStream();
                String html = convertInputStreamToString(inputStream);
                Mail mail = new Mail();
                mail.setTo(email);
                mail.setContent(html);
                mail.setProcessEmail(true);
                mail.setSubject(subject);
                emailService.sendEmail(mail);
            } else {
                throw new ReceiptStorageException("Receipt does not exist in the DB");
            }
        } catch (IOException e) {
            throw new EmailServiceException("Error while saving image / sending mail");
        }
        LOG.info("Reprint Receipt Function Ends");
    }

    @Override
    public Template retrieveReceipt(String merchantId, String stan) throws IOException {
        LOG.info("Retrieve Receipt Function got invoked");
        Template tem = null;
        GridFSFile fsFile = gridFsTemplate.findOne(new Query(Criteria.where("metadata.merchantId").is(merchantId).and("metadata.stan").is(stan)));
        if (fsFile != null) {
            tem = new Template();
            tem.setTitle(fsFile.getMetadata().get("type").toString());
            tem.setInputStream(gridFsOperations.getResource(fsFile).getInputStream());
        } else {
            throw new ReceiptStorageException("Receipt does not exist in the DB");
        }
        LOG.info("Retrieve Receipt Function Ends");
        return tem;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }
        return sb.toString();
    }

    @Override
    public List<String> getListOfTemplates() {
        List<GridFSFile> fsFiles = new ArrayList<>();
        gridFsTemplate.find(new Query(Criteria.where("metadata.fileType").is(TEMPLATE_FILE_TYPE))).into(fsFiles);
        List<String> fileNames = new ArrayList<>();
        if (!(fsFiles.isEmpty())) {
            for (GridFSFile fs : fsFiles) {
                fileNames.add(fs.getFilename());
            }
        } else {
            throw new FileStorageException("Templates does not exist in DB");
        }
        return fileNames;
    }

}