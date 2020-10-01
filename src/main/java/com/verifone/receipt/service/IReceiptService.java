/**********************************************************************
 * The ReceiptService interface is related to get the receipt          *
 * related Service operation.                                          *
 *                                                                     *
 * @version 1.0                                                        *
 * @since 06/04/2019                                                 *
 **********************************************************************/
package com.verifone.receipt.service;

import com.verifone.api.receipt.model.ReceiptTransaction;
import com.verifone.api.receipt.model.SendReceipt;
import com.verifone.receipt.exception.EmailServiceException;
import com.verifone.receipt.exception.InvalidTemplateDataException;
import com.verifone.receipt.exception.InvalidTemplateException;
import com.verifone.receipt.model.Template;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IReceiptService {

    public void processReceipt(ReceiptTransaction receiptTransaction)
            throws InvalidTemplateDataException, EmailServiceException, InvalidTemplateException;

    public void sendHTMLReceipt(SendReceipt sendReceipt)
            throws InvalidTemplateDataException, EmailServiceException, InvalidTemplateException;

    public String saveTemplate(String templateString) throws EmailServiceException;

    public String addTemplate(MultipartFile file) throws IOException;

    public Template retrieveTemplate(String filename) throws IOException;

    public String updateTemplate(MultipartFile file, String filename)throws IOException;

    public String deleteTemplate(String filename)throws IOException;

    public void retrieveAllTemplate() throws IOException;

    public void reprintReceipt(ReceiptTransaction receiptTransaction) throws EmailServiceException;

    public Template retrieveReceipt(String merchantId, String stan) throws IOException;

    public List<String> getListOfTemplates();

}

