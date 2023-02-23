package com.example.one_mw.exception;

public class TaskTerminationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskTerminationException(String errorMsg) {
		super(errorMsg);
	}
}
