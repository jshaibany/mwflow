package com.example.one_mw.exception;

@SuppressWarnings("serial")
public class ProcessExpressionEvaluationException extends Exception {

	public ProcessExpressionEvaluationException(String errorMsg) {
		super(errorMsg);
	}
}
