package com.example.one_mw.exception;

public abstract class InvalidEntityException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidEntityException(String errorMsg) {
		super(errorMsg);
	}

}
