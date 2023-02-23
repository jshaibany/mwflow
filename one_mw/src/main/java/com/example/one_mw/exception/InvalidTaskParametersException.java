package com.example.one_mw.exception;

public class InvalidTaskParametersException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidTaskParametersException(String errorMsg) {
		super(errorMsg);
	}

}
