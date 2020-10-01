/**********************************************************************
 * The ReceiptServiceController class  is related to                   *
 * all the incoming request for the receipt related rest call.         *
 *                                                                     *
 * @version 1.0                                                        *
 * @since 06/04/2019                                                 *
 **********************************************************************/
package com.verifone.receipt.endpoint;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.verifone.api.receipt.model.ReceiptTransaction;
import com.verifone.api.receipt.model.SendReceipt;
import com.verifone.receipt.exception.*;
import com.verifone.receipt.model.Template;
import com.verifone.receipt.service.IReceiptService;
import com.verifone.receipt.validation.SizeLimitValidation;
import com.verifone.svc.framework.endpoint.server.SvcServerEndpoint;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@SvcServerEndpoint(id = "receipts", basePath = "/receipts")
@Log4j2
public class ReceiptServiceEndpoint {

    @Autowired
    private IReceiptService receiptService;

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity sendReceipt(@RequestBody ReceiptTransaction receiptTransaction) {
        log.info("processing send receipt endpoint");
        try {
            final Long startTime = System.currentTimeMillis();
            receiptService.processReceipt(receiptTransaction);
            log.info("Total time taken Receipt Services in ms : {}", (System.currentTimeMillis() - startTime));
            return new ResponseEntity(generateResponse(SizeLimitValidation.SUCCESS_CODE), HttpStatus.CREATED);
        } catch (InvalidTemplateDataException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(SizeLimitValidation.INVALID_API),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (InvalidTemplateException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(SizeLimitValidation.INVALID_TEMPLATE),
                    HttpStatus.FAILED_DEPENDENCY);
        } catch (EmailServiceException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(SizeLimitValidation.INVALID_EMAIL_PROCESS),
                    HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(SizeLimitValidation.UNKNOWN_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "preformatted", consumes = "application/json", produces = "application/json")
    public ResponseEntity sendHTMLEmail(@RequestBody SendReceipt sendReceipt) {
        log.info("processing send Email endpoint");
        try {
            receiptService.sendHTMLReceipt(sendReceipt);
            return new ResponseEntity(generateResponse(SizeLimitValidation.SUCCESS_CODE), HttpStatus.CREATED);
        } catch (InvalidTemplateDataException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(SizeLimitValidation.INVALID_API),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (InvalidTemplateException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(SizeLimitValidation.INVALID_TEMPLATE),
                    HttpStatus.FAILED_DEPENDENCY);
        } catch (EmailServiceException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(SizeLimitValidation.INVALID_EMAIL_PROCESS),
                    HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(SizeLimitValidation.UNKNOWN_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "saveTemplate", consumes = {MediaType.TEXT_HTML_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity saveTemplate(@RequestBody String templateString) {
        log.info("Save Template endpoint");
        try {
            return new ResponseEntity(receiptService.saveTemplate(templateString), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ReceiptServiceException> invalidFormatException(final InvalidFormatException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity(generateResponse(SizeLimitValidation.INVALID_API),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({UnrecognizedPropertyException.class})
    public ResponseEntity handleException() {
        log.error("Extra Field on Request Data");
        return new ResponseEntity(generateResponse(SizeLimitValidation.INVALID_API),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private Map<String, Object> generateResponse(String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

    @PostMapping(path = "addTemplate")
    public ResponseEntity addTemplate(@RequestParam("file") MultipartFile file) {
        log.info("Processing Add Template");
        try {
            receiptService.addTemplate(file);
            log.info("Added Template successfully into Mongo DB");
            return new ResponseEntity(generateResponse(SizeLimitValidation.SUCCESS_CODE), HttpStatus.CREATED);
        } catch (FileStorageException | IOException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(e.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(value = "retrieveTemplate/{filename}")
    public ResponseEntity retrieveTemplate(@PathVariable String filename, HttpServletResponse response) {
        try {
            Template template = receiptService.retrieveTemplate(filename);
            log.info("retrieved Template successfully from Mongo DB");
            FileCopyUtils.copy(template.getInputStream(), response.getOutputStream());
            return new ResponseEntity(generateResponse(SizeLimitValidation.SUCCESS_CODE), HttpStatus.CREATED);
        } catch (FileStorageException | IOException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(e.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(value = "updateTemplate/{filename}")
    public ResponseEntity updateTemplate(@RequestParam("file") MultipartFile file, @PathVariable String filename) {
        try {
            receiptService.updateTemplate(file, filename);
            log.info("Updated Template Successfully ");
            return new ResponseEntity(generateResponse(SizeLimitValidation.SUCCESS_CODE), HttpStatus.CREATED);
        } catch (FileStorageException | IOException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(e.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping(value = "deleteTemplate/{filename}")
    public ResponseEntity deleteTemplate(@PathVariable String filename) {
        try {
            receiptService.deleteTemplate(filename);
            log.info("Deleted Template Successfully");
            return new ResponseEntity(generateResponse(SizeLimitValidation.SUCCESS_CODE), HttpStatus.CREATED);
        } catch (FileStorageException | IOException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(e.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(value = "retrieveAllTemplate")
    public ResponseEntity retrieveAllTemplate() {
        log.info("Processing retrieveTemplate endpoint");
        try {
            receiptService.retrieveAllTemplate();
            log.info("retrieved Templates successfully from Mongo DB ");
            return new ResponseEntity(generateResponse(SizeLimitValidation.SUCCESS_CODE), HttpStatus.CREATED);
        } catch (FileStorageException | IOException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(e.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = "reprintReceipt")
    public ResponseEntity reprintReceipt(@RequestBody ReceiptTransaction receiptTransaction) {
        log.info("Processing reprinted Receipt");
        try {
            receiptService.reprintReceipt(receiptTransaction);
            log.info("retrieved Receipt from DB and sent reprinted receipt successfully");
            return new ResponseEntity(generateResponse(SizeLimitValidation.SUCCESS_CODE), HttpStatus.CREATED);
        } catch (ReceiptStorageException | EmailServiceException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(e.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(value = "retrieveReceipt/{merchantId}/{stan}")
    public ResponseEntity retrieveReceipt(@PathVariable String merchantId, @PathVariable String stan, HttpServletResponse response) {
        log.info("Processing retrieve Receipt");
        try {
            Template template = receiptService.retrieveReceipt(merchantId, stan);
            FileCopyUtils.copy(template.getInputStream(), response.getOutputStream());
            log.info("retrieved Receipt from DB");
            return new ResponseEntity(generateResponse(SizeLimitValidation.SUCCESS_CODE), HttpStatus.CREATED);
        } catch (ReceiptStorageException | IOException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(e.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(value = "getListOfTemplates")
    public ResponseEntity<List<String>> getListOfTemplates() {
        log.info("Processing All Templates");
        try {
            List<String> fileNames = receiptService.getListOfTemplates();
            log.info("retrieved All Templates from DB");
            return ResponseEntity.status(HttpStatus.OK).body(fileNames);
        } catch (FileStorageException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(generateResponse(e.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}