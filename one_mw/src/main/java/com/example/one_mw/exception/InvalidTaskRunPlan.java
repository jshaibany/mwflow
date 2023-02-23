package com.example.one_mw.exception;

public class InvalidTaskRunPlan extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTaskRunPlan(String errorMsg) {
		super(errorMsg);
	}
}
