package com.example.one_mw.exception;

public class FailedToFindProcessNameException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FailedToFindProcessNameException(String errorMsg) {
		super(errorMsg);
	}
}
