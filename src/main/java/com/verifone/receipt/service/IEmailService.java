/**********************************************************************
* The EmailService interface is related to get the email              *
* related Service operation.                                          *
*                                                                     *
* @version 1.0                                                        *
* @since   06/04/2019                                                 *
**********************************************************************/
package com.verifone.receipt.service;

import com.verifone.receipt.exception.EmailServiceException;
import com.verifone.receipt.model.Mail;

public interface IEmailService {

	public String sendEmailSimple(Mail mail) throws EmailServiceException;
	public String sendEmail(Mail mail) throws EmailServiceException;

}
