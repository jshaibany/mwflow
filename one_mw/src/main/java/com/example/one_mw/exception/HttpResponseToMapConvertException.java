package com.example.one_mw.exception;

@SuppressWarnings("serial")
public class HttpResponseToMapConvertException extends Exception {

	public HttpResponseToMapConvertException(String errorMsg) {
		super(errorMsg);
	}
}
