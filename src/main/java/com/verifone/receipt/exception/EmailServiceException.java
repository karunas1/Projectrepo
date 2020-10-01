/**********************************************************************
* The EmailServiceException class is related to handle 		          *
* all the exception that causes during Service Layer.                 *
*                                                                     *
* @version 1.0                                                        *
* @since   06/04/2019                                                 *
**********************************************************************/
package com.verifone.receipt.exception;

public class EmailServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4976027948501532988L;

	public EmailServiceException() {
	}

	/**
	 * @param message
	 */
	public EmailServiceException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public EmailServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EmailServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
