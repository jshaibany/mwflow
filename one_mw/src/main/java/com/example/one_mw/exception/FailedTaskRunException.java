package com.example.one_mw.exception;

public class FailedTaskRunException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FailedTaskRunException(String errorMsg) {
		super(errorMsg);
	}
}
