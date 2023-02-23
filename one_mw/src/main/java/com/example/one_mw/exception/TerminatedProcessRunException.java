package com.example.one_mw.exception;

public class TerminatedProcessRunException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TerminatedProcessRunException(String errorMsg) {
		super(errorMsg);
	}
}
