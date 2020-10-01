/**********************************************************************
* The DaoException class is related to handle all the exception		  *
* that causes during DAO Layer.                                       *
*                                                                     *
* @version 1.0                                                        *
* @since   06/04/2019                                                 *
**********************************************************************/
package com.verifone.receipt.exception;

public class DaoException extends Exception {

	private static final long serialVersionUID = 927487354603184737L;

	public DaoException() {
		super();
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

	protected DaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
