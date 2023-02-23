package com.example.one_mw.service.runtime;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.one_mw.entity.IntegratedService;
import com.example.one_mw.entity.Task;
import com.example.one_mw.exception.FailedRequestException;
import com.example.one_mw.exception.HttpResponseToMapConvertException;
import com.example.one_mw.exception.InvalidHttpRequestMethod;
import com.example.one_mw.exception.MapExpressionEvaluationException;
import com.example.one_mw.schema.converters.JsonToMapConverter;
import com.example.one_mw.service.integrated.IHttpObjRequestable;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class MWRestfullEndPointsRunTime implements IntegratedServicesObjRunTime {

	Logger logger = LogManager.getLogger(MWRestfullEndPointsRunTime.class);
	
	private ApplicationContext context;
	private IHttpObjRequestable request;
	private IntegratedService service;
	
	@Override
	public void buildContext(IntegratedService service) {
		
		logger.info(String.format("Integrated service build context:"));
		
		this.service = service;
		
		this.context = new ClassPathXmlApplicationContext(service.getTypeXMLFile());
		
		this.request = (IHttpObjRequestable) context.getBean(service.getType());
		
		logger.info(String.format("Integrated service context type file", service.getTypeXMLFile()));
		logger.info(String.format("Integrated service bean type", service.getType()));
	}
	
	@Override
	public Map<String, Object> doRequest(Map<String, Object> map,Task task) throws MapExpressionEvaluationException, InvalidHttpRequestMethod, HttpResponseToMapConvertException, FailedRequestException {
		
		request.buildContext(this.service.getCredentialsXMLFile(), this.service.getCredentialsId());
		request.buildHeaders();
		request.buildBodyRequest(task.getRequestBody(), map);
		
		String response;
		try {
			
			logger.info(String.format("Integrated service trying to request and get response .."));
			
			response = request.request(this.service.getMethod());
			
			logger.info(String.format("Integrated service response:"));
			logger.info(String.format("%s",response));
		}
		catch(Exception e) {
			
			logger.error(String.format("Integrated service request error:"));
			logger.error(String.format("%s",e.getMessage()));
			
			e.printStackTrace();
			throw new FailedRequestException("");
		}
		
		
		
		switch(service.getResponseType()) {
		
		case "JSON":
			try {
				
				logger.info(String.format("Integrated service convert %s response into map schema ..",service.getResponseType()));
				
				return JsonToMapConverter.convertJsonToMap(response);
			} 
			catch(MismatchedInputException e) {
				
				logger.error(String.format("Integrated service request error:"));
				logger.error(String.format("%s",e.getMessage()));
				
				e.printStackTrace();
				throw new HttpResponseToMapConvertException("HttpResponseToMapConvertException");
				
			}
			catch (Exception e) {
				
				logger.error(String.format("Integrated service request error:"));
				logger.error(String.format("%s",e.getMessage()));
				
				e.printStackTrace();
				throw new HttpResponseToMapConvertException("HttpResponseToMapConvertException");
			}
			
		default:
			
			throw new HttpResponseToMapConvertException("HttpResponseToMapConvertException");
		}
		
		
	}
}
