package com.example.one_mw.service.integrated;

import java.util.Map;

import com.example.one_mw.exception.FailedRequestException;
import com.example.one_mw.exception.InvalidHttpRequestMethod;
import com.example.one_mw.exception.MapExpressionEvaluationException;

/**
 * .
 *
 * @author Galal M.Ahmed
 */

public interface IHttpRequestable {

	/*
	 * (non-Javadoc)
	 * @see 
	 */
	public void buildContext(String beanXML,String beanID);
	/*
	 * (non-Javadoc)
	 * @see 
	 */
	public void buildHeaders();
	/*
	 * (non-Javadoc)
	 * @see 
	 */
	public void buildBodyRequest(String body,Map<String,String> map) throws MapExpressionEvaluationException;
	/*
	 * (non-Javadoc)
	 * @see 
	 */
	public String request(String method) throws InvalidHttpRequestMethod, FailedRequestException;
	
}
