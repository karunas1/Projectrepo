/**********************************************************************
 * The HtmlFormattingService interface is related to get the format    *
 * the  HTML template.                                                 *
 *                                                                     *
 * @version 1.0                                                        *
 * @since 06/04/2019                                                   *
 **********************************************************************/
package com.verifone.receipt.service;

import com.verifone.api.receipt.model.ReceiptTransaction;
import com.verifone.api.receipt.model.SendReceipt;
import com.verifone.receipt.exception.InvalidTemplateDataException;
import com.verifone.receipt.model.Mail;

public interface IHtmlFormattingService {

	public void formatPreformatted(SendReceipt sendReceipt, Mail mail) throws InvalidTemplateDataException;
	public void formatReceipt(ReceiptTransaction receiptTransaction, Mail mail) throws InvalidTemplateDataException;
	public String saveImage(String html) throws InvalidTemplateDataException;
	public String convertHtmltoPDF(String html) throws InvalidTemplateDataException;

}
