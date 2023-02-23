package com.example.one_mw.exception;

public class FailedRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FailedRequestException(String errorMsg) {
		super(errorMsg);
	}
}
