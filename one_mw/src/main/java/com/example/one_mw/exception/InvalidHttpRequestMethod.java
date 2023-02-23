package com.example.one_mw.exception;

@SuppressWarnings("serial")
public class InvalidHttpRequestMethod extends Exception{

	public InvalidHttpRequestMethod(String errorMsg) {
		super(errorMsg);
	}
}
