
/**********************************************************************
 * The HtmlFormattingServiceImpl class is related to get the format    *
 * the  HTML template.                                                 *
 *                                                                     *
 * @version 1.0                                                        *
 * @since 06/04/2019                                                   *
 **********************************************************************/
package com.verifone.receipt.serviceimpl;

import com.verifone.api.receipt.model.*;
import com.verifone.cegp.security.StringUtil;
import com.verifone.receipt.config.CustomHTMLEditorKit;
import com.verifone.receipt.constant.ReceiptConstants;
import com.verifone.receipt.exception.InvalidTemplateDataException;
import com.verifone.receipt.model.Mail;
import com.verifone.receipt.service.IHtmlFormattingService;
import com.verifone.receipt.validation.SizeLimitValidation;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static com.verifone.receipt.constant.ReceiptConstants.FALSE;
import static com.verifone.receipt.constant.ReceiptConstants.TEMPLATES;

@Service
public class HtmlFormattingServiceImpl implements IHtmlFormattingService {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlFormattingServiceImpl.class);
    private static final String IMAGE_SRC = "<img src=\"";

    @Value("${receipt.email.attachment}")
    private String emailAttachment;

    @Value("${receipt.preformatted.attachment}")
    private String preFormatAttachment;

    @Value("${receipt.file.location}")
    private String fileLocation;

    @Value("${receipt.file.chromeLocation}")
    private String chromeLocation;

    @Value("${receipt.cmd.htmlToPdfCmd}")
    private String htmlToPdfCmd;

    @Value("${receipt.cmd.htmlToPngCmd}")
    private String htmlToPngCmd;

    @Value("${receipt.htmlRender.cmdtype}")
    private String cmdtype;

    @Override
    public void formatPreformatted(SendReceipt sendReceipt, Mail mail) throws InvalidTemplateDataException {

        Configuration cfg = new Configuration(new Version("2.3.23"));

        Template t = null;
        Map<String, Object> tempData = new HashMap<>();

        try {
            FileTemplateLoader ftl1 = new FileTemplateLoader(new File(fileLocation + "/templates/"));
            MultiTemplateLoader mtl = new MultiTemplateLoader(new TemplateLoader[]{ftl1});
            cfg.setTemplateLoader(mtl);

            java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
            String content = new String(decoder.decode(sendReceipt.getContent()));
            content = updateStyle(content);
            convertHtmlStringtoFile(content);
            String imageName = convertHtmltoPNG();
            t = cfg.getTemplate("sendHtml.ftl");
            tempData.put("sendEmailContent", IMAGE_SRC + "cid:sendEmailContent\"/>");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, tempData);
            mail.setImageName(imageName);
            mail.setContent(html);
            convertHtmltoPDF(content);
            mail.setProcessEmail(false);
        } catch (Exception e) {
            throw new InvalidTemplateDataException(ReceiptConstants.HTML_FORMAT_ERR, e);
        }
    }

    private String updateStyle(String html) {

        Document doc = Jsoup.parse(html);
        //doc.head().append("@media print {@page {margin: 0;} body {margin: 1.6cm;}}");
        doc.head().appendElement("style").append("@media print {@page {margin: 0;} body {margin: 1.6cm;}}");

        return doc.html();
    }

    public String convertHtmltoPDF(String content) {
        ProcessBuilder processBuilder = new ProcessBuilder();

        StringBuilder command = new StringBuilder();
        command.append(htmlToPdfCmd);
        command.append(fileLocation + TEMPLATES + "output.pdf ");
        command.append(fileLocation + TEMPLATES + "preformat.html ");

        if ("windows".equalsIgnoreCase(cmdtype)) {
            File directory = new File(chromeLocation);
            processBuilder.directory(directory);
            processBuilder.command("cmd.exe", "/c", command.toString());
        } else {
            File directory = new File(fileLocation);
            processBuilder.directory(directory);
            processBuilder.command("bash", "-c", command.toString());
        }
        try {
            Process process = processBuilder.start();

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                LOG.info("Succesfully able to create pdf");
            } else {
                LOG.info("Failed to create pdf");
            }

        } catch (IOException e) {
            LOG.error("Exception while converting Html to pdf ", e);
        } catch (Exception e) {
            LOG.error("Interrupted Exception while converting Html to pdf ", e);
        }
        return "output.pdf";
    }

    public String convertHtmltoPNG() {
        ProcessBuilder processBuilder = new ProcessBuilder();

        StringBuilder command = new StringBuilder();
        command.append(htmlToPngCmd);
        command.append(fileLocation + TEMPLATES + "output.png ");
        command.append("--window-size=1024,1280 ");
        command.append(fileLocation + TEMPLATES + "preformat.html ");

        if ("windows".equalsIgnoreCase(cmdtype)) {
            File directory = new File(chromeLocation);
            processBuilder.directory(directory);
            processBuilder.command("cmd.exe", "/c", command.toString());
        } else {
            File directory = new File(fileLocation);
            processBuilder.directory(directory);
            processBuilder.command("bash", "-c", command.toString());
        }
        try {
            Process process = processBuilder.start();

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                LOG.info("Succesfully able to create png");
            } else {
                LOG.info("Failed to create pdf");
            }

        } catch (IOException e) {
            LOG.error("Exception while converting Html to png " ,e);
        } catch (Exception e) {
            LOG.error("Interrupted Exception while converting Html to png " ,e);
        }
        return "output.png";
    }

    private void convertHtmlStringtoFile(String content) throws InvalidTemplateDataException {

        File file = new File(fileLocation + TEMPLATES + "preformat.html");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            throw new InvalidTemplateDataException("Error while converting content into file");
        }
    }


    private void fetchDataFromTemplate(ReceiptTransaction receiptTransaction, Mail mail, String html) throws InvalidTemplateDataException {
        Document doc = Jsoup.parse(html);
        Elements metaTags = doc.getElementsByTag("meta");

        for (Element metaTag : metaTags) {
            String field = metaTag.attr("name");
            String value = metaTag.attr("content");

            if ("defaultSubject".equalsIgnoreCase(field) && Objects.isNull(receiptTransaction.getSubject())) {
                mail.setSubject(value);
            }

            if ("transactionType".equalsIgnoreCase(field) && (!value.contains(receiptTransaction.getTransaction().getTransactionType().toString()))) {
                throw new InvalidTemplateDataException(
                        "TransactionType " + receiptTransaction.getTransaction().getTransactionType().toString() + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID);
            }
        }
    }

    @Override
    public void formatReceipt(ReceiptTransaction receiptTransaction, Mail mail) throws InvalidTemplateDataException {
        String html = null;

        try {
            Configuration cfg = new Configuration(new Version("2.3.23"));
            Map<String, Object> tempData = new HashMap<>();

            FileTemplateLoader ftl1 = new FileTemplateLoader(new File(fileLocation + "/templates/"));
            MultiTemplateLoader mtl = new MultiTemplateLoader(new TemplateLoader[]{ftl1});
            cfg.setTemplateLoader(mtl);

            tempData = setLabel(tempData, "EN");
            tempData = setTemplateData(receiptTransaction, tempData);

            Template t = cfg.getTemplate(receiptTransaction.getTemplateId() + ".ftl");

            if ("yes".equalsIgnoreCase(emailAttachment)) {
                html = FreeMarkerTemplateUtils.processTemplateIntoString(t, tempData);
                String imageName = saveImage(html);
                mail.setImageName(imageName);
                mail.setContent(html);
                mail.setProcessEmail(true);
            } else if ("no".equalsIgnoreCase(emailAttachment)) {

                html = FreeMarkerTemplateUtils.processTemplateIntoString(t, tempData);
                mail.setContent(html);
                mail.setProcessEmail(true);
            }

            fetchDataFromTemplate(receiptTransaction, mail, html);
        } catch (Exception e) {
            throw new InvalidTemplateDataException(ReceiptConstants.HTML_FORMAT_ERR, e);
        }
    }

    private Map<String, Object> setLabel(Map<String, Object> tempData, String lang)
            throws InvalidTemplateDataException {
        if ("EN".equals(lang)) {
            try {
                Properties properties = new Properties();
                InputStream in = new FileInputStream(fileLocation + "templateLabel_EN.properties");
                properties.load(in);
                tempData.put("label_terminal_id", properties.get("label_terminal_id"));
                tempData.put("label_merchant_id", properties.get("label_merchant_id"));
                tempData.put("label_datetime", properties.get("label_datetime"));
                tempData.put("label_stan", properties.get("label_stan"));
                tempData.put("label_auth", properties.get("label_auth"));
                tempData.put("label_aid", properties.get("label_aid"));
                tempData.put("label_tvr", properties.get("label_tvr"));
                tempData.put("label_atc", properties.get("label_atc"));
                tempData.put("label_card", properties.get("label_card"));
                tempData.put("label_card_brand", properties.get("label_card_brand"));
                tempData.put("label_currency_code", properties.get("label_currency_code"));
                tempData.put("label_created_date_time", properties.get("label_created_date_time"));
                tempData.put("label_merchant_reference", properties.get("label_merchant_reference"));
                tempData.put("label_url", properties.get("label_url"));
                tempData.put("label_txn", properties.get("label_txn"));
                tempData.put("label_cashout", properties.get("label_cashout"));
                tempData.put("label_tip", properties.get("label_tip"));
                tempData.put("label_surcharge", properties.get("label_surcharge"));
                tempData.put("label_total", properties.get("label_total"));
                tempData.put("label_customer_copy", properties.get("label_customer_copy"));
                tempData.put("label_duplicate_copy", properties.get("label_duplicate_copy"));
                tempData.put("label_merchant_copy", properties.get("label_merchant_copy"));
            } catch (Exception e) {
                throw new InvalidTemplateDataException("Label Language is not configure");
            }
        }
        return tempData;
    }

    private Map<String, Object> setTemplateData(ReceiptTransaction receiptTransaction, Map<String, Object> tempData) {
        Metadata metadata = receiptTransaction.getMetadata();
        Transaction transaction = receiptTransaction.getTransaction();
        Merchant merchant = receiptTransaction.getMerchant();
        metadataValues(tempData, metadata);
        if (transaction != null) {
            ICC icc = transaction.getIcc();
            tempData.put("transaction.cardBrand", returnStringIfEmpty(transaction.getCardBrand()));
            //Need to change value for threeDSecure mapping transaction.getThreeDSecure() till then setting TRUE
            boolean is3DSecure = transaction.isThreeDSecure();
            tempData.put("transaction.threeDSecure",  is3DSecure ? "TRUE" : "FALSE");
            tempData.put("transaction.fundingSource", (transaction.getFundingSource() == null ? "" : transaction.getFundingSource()));
            tempData.put("transaction.acquirerName", returnStringIfEmpty(transaction.getAcquirerName()));
            tempData.put("transaction.initiatorTraceId", returnStringIfEmpty(transaction.getInitiatorTraceId()));
            tempData.put("transaction.maskedCardNumber", returnStringIfEmpty(transaction.getMaskedCardNumber()));
            tempData.put("transaction.transactionType", (transaction.getTransactionType() == null) ? "" : transaction.getTransactionType().toString());
            tempData.put("transaction.accountType", (transaction.getAccountType() == null) ? "" : transaction.getAccountType().toString());
            tempData.put("transaction.entryMode", (transaction.getEntryMode() == null) ? "" : transaction.getEntryMode().toString());
            tempData.put("transaction.applicationPreferredName",
                    returnStringIfEmpty(transaction.getApplicationPreferredName()));
            tempData.put("transaction.amount", transaction.getAmount() != null ? transaction.getAmount() : -1.0);
            tempData.put("transaction.responseCode", returnStringIfEmpty(transaction.getResponseCode()));
            tempData.put("transaction.errorMessage", returnStringIfEmpty(transaction.getErrorMessage()));

            calculateTransmittedDateTime(tempData, transaction);

            calculateCreatedDateTime(tempData, transaction.getCreatedDateTime(), "transaction.createdDateTime");
            transactionAmountValues(tempData, transaction);
            iccValues(tempData, icc);
        }
        merchantValues(tempData, merchant);

        tempData.put("logoContent", IMAGE_SRC + "cid:logoContent\"/>");
        tempData.put("signature_sample", IMAGE_SRC + "cid:signature_sample\"/>");
        tempData.put("cm5_advertisement", IMAGE_SRC + "cid:cm5_advertisement\"/>");

        return tempData;
    }

    private void transactionAmountValues(Map<String, Object> tempData, Transaction transaction) {
        tempData.put("transaction.currencyCode", (transaction.getCurrencyCode() == null ? "" : transaction.getCurrencyCode()));
        tempData.put("transaction.merchantReference", returnStringIfEmpty(transaction.getMerchantReference()));
        tempData.put("transaction.authorisationCode", returnStringIfEmpty(transaction.getAuthorisationCode()));
        tempData.put("transaction.cashbackAmount",
                transaction.getCashbackAmount() != null ? transaction.getCashbackAmount() : -1.0);
        tempData.put("transaction.gratuityAmount",
                transaction.getGratuityAmount() != null ? transaction.getGratuityAmount() : -1.0);
        tempData.put("transaction.feeAmount",
                transaction.getFeeAmount() != null ? transaction.getFeeAmount() : -1.0);
        tempData.put("transaction.totalAmount",
                transaction.getTotalAmount() != null ? transaction.getTotalAmount() : -1.0);
        tempData.put("transaction.dedicatedFileName", transaction.getDedicatedFileName());
    }

    private void merchantValues(Map<String, Object> tempData, Merchant merchant) {
        if (merchant != null) {

            tempData.put("merchant.name", returnStringIfEmpty(merchant.getName()));
            tempData.put("merchant.address", returnStringIfEmpty(merchant.getAddress()));
            tempData.put("merchant.postCode", returnStringIfEmpty(merchant.getPostCode()));
            tempData.put("merchant.city", returnStringIfEmpty(merchant.getCity()));
            tempData.put("merchant.phoneNumbers", returnStringIfEmpty(merchant.getPhoneNumbers()));
            tempData.put("merchant.country", (merchant.getCountry() == null) ? "" : merchant.getCountry().toString());
            tempData.put("merchant.poiId", returnStringIfEmpty(merchant.getPoiId()));
            tempData.put("merchant.merchantId", returnStringIfEmpty(merchant.getMerchantId()));
            tempData.put("merchant.URL", returnStringIfEmpty(merchant.getURL()));
        }
    }

    private void iccValues(Map<String, Object> tempData, ICC icc) {
        if (icc != null) {
            tempData.put("transaction.icc.debugData", returnStringIfEmpty(icc.getDebugData()));
            tempData.put("transaction.icc.sequenceNumber", returnStringIfEmpty(icc.getSequenceNumber()));
            tempData.put("transaction.icc.applicationId", returnStringIfEmpty(icc.getApplicationId()));
            tempData.put("transaction.icc.terminalVerificationResults",
                    returnStringIfEmpty(icc.getTerminalVerificationResults()));
            tempData.put("transaction.icc.expectedApplicationTransactionCounter",
                    returnStringIfEmpty(icc.getExpectedApplicationTransactionCounter()));
            tempData.put("transaction.icc.cryptogramInformationData",
                    returnStringIfEmpty(icc.getCryptogramInformationData()));
        }
    }

    private void metadataValues(Map<String, Object> tempData, Metadata metadata) {
        if (metadata != null) {
            tempData.put("metadata.headerText", returnStringIfEmpty(metadata.getHeaderText()));
            tempData.put("metadata.footerText", returnStringIfEmpty(metadata.getFooterText()));
            tempData.put("metadata.receiptCopyType", returnStringIfEmpty(metadata.getReceiptCopyType()));
            tempData.put("metadata.psnAvailable", metadata.isPsnAvailable());
            tempData.put("metadata.cryptoType", returnStringIfEmpty(metadata.getCryptoType()));
            tempData.put("metadata.txnApprovalText", returnStringIfEmpty(metadata.getTxnApprovalText()));

        }
    }

    private void calculateTransmittedDateTime(Map<String, Object> tempData, Transaction transaction) {
        if (transaction.getTransmittedDateTime() != null) {
            try {
                tempData.put("transaction.transmittedDateTime",
                        transaction.getTransmittedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } catch (DateTimeException e) {
                try {
                    throw new InvalidTemplateDataException(
                            transaction.getTransmittedDateTime().toString() + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID, e);
                } catch (InvalidTemplateDataException ex) {
                    LOG.error(ex.getMessage());
                }
            }
        } else {
            tempData.put("transaction.transmittedDateTime", FALSE);
        }
    }

    private void calculateCreatedDateTime(Map<String, Object> tempData, OffsetDateTime createdDateTime, String s) {
        if (createdDateTime != null) {
            try {
                tempData.put(s,
                        createdDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } catch (DateTimeException e) {
                try {
                    throw new InvalidTemplateDataException(
                            createdDateTime.toString() + SizeLimitValidation.SPACE + SizeLimitValidation.INVALID, e);
                } catch (InvalidTemplateDataException ex) {
                    LOG.error(ex.getMessage());
                }
            }
        } else {
            tempData.put(s, FALSE);
        }
    }

    private String returnStringIfEmpty(String value) {
        return (!StringUtil.isNullOrEmpty(value) ? value : "");
    }


    @Override
    public String saveImage(String html) throws InvalidTemplateDataException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(html.getBytes());
            String cnvtImage = "img" + new BigInteger(1, messageDigest).toString();
            File file1 = new File(fileLocation + TEMPLATES + cnvtImage + ".png");
            if (!file1.exists()) {
                JEditorPane jep = new JEditorPane();
                jep.setEditable(false);
                jep.setContentType("text/html");
                jep.setEditorKit(new CustomHTMLEditorKit());
                jep.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
                jep.setText(html);

                jep.setSize(jep.getPreferredSize());
                BufferedImage image = new BufferedImage(jep.getWidth(), jep.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                JPanel container = new JPanel();
                SwingUtilities.paintComponent(g, jep, container, 0, 0, image.getWidth(), image.getHeight());
                g.dispose();
                ImageIO.write(image, "png", file1);
            }
            return cnvtImage + ".png";
        } catch (Exception e) {
            throw new InvalidTemplateDataException("Error while saving Image from the HTML receipt", e);
        }
    }
}