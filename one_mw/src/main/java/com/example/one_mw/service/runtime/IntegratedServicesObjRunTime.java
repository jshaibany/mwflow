package com.example.one_mw.service.runtime;

import java.util.Map;

import com.example.one_mw.entity.IntegratedService;
import com.example.one_mw.entity.Task;
import com.example.one_mw.exception.FailedRequestException;
import com.example.one_mw.exception.HttpResponseToMapConvertException;
import com.example.one_mw.exception.InvalidHttpRequestMethod;
import com.example.one_mw.exception.MapExpressionEvaluationException;

public interface IntegratedServicesObjRunTime {

	public void buildContext(IntegratedService service);
	public Map<String,Object> doRequest(Map<String,Object> map,Task task) throws MapExpressionEvaluationException,InvalidHttpRequestMethod, HttpResponseToMapConvertException, FailedRequestException;
	
}
