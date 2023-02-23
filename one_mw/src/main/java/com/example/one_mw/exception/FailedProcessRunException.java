package com.example.one_mw.exception;

public class FailedProcessRunException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FailedProcessRunException(String errorMsg) {
		super(errorMsg);
	}
}
