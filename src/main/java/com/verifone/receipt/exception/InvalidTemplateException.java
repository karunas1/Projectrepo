/**********************************************************************
* The InvalidTemplateException class is related to handle 		      *
* all the exception that causes during Service Layer.                 *
*                                                                     *
* @version 1.0                                                        *
* @since   06/04/2019                                                 *
**********************************************************************/
package com.verifone.receipt.exception;

public class InvalidTemplateException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -4976027948501532988L;

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

	public InvalidTemplateException() {
	}

	/**
	 * @param message
	 */
	public InvalidTemplateException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidTemplateException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidTemplateException(String message, Throwable cause) {
		super(message, cause);
	}
}
