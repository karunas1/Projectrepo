/**********************************************************************
* The SMTPConfigDAO class is related to get the SMTP				  *
* configuration  from Database.                                       *
*                                                                     *
* @version 1.0                                                        *
* @since   06/04/2019                                                 *
**********************************************************************/
package com.verifone.receipt.dao;

import com.verifone.receipt.exception.DaoException;
import com.verifone.receipt.model.SMTPConfig;

public interface SMTPConfigDAO {
	public SMTPConfig getSMTPConfDetails() throws DaoException;
}
