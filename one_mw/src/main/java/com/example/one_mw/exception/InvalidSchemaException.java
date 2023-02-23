package com.example.one_mw.exception;

public abstract class InvalidSchemaException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidSchemaException(String errorMsg) {
		super(errorMsg);
	}

	
}
