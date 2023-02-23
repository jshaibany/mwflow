package com.example.one_mw.service.integrated;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.example.one_mw.exception.FailedRequestException;
import com.example.one_mw.exception.InvalidHttpRequestMethod;
import com.example.one_mw.exception.MapExpressionEvaluationException;
import com.example.one_mw.service.LanguageExpressionService;

public class TCSDirectXMLObjRequest implements IHttpObjRequestable{

	Logger logger = LogManager.getLogger(TCSDirectXMLObjRequest.class);
	
	private HttpHeaders headers;
	private Object xmlBody;
	private ICredentialsConnector connector;
	private ApplicationContext context;
	
	@Override
	public void buildContext(String beanXML, String beanID) {
		
		context 
	      = new ClassPathXmlApplicationContext(beanXML);
		connector = (ICredentialsConnector) context.getBean(beanID);
		
		logger.info(String.format("[%s] context Connector XML file [%s]",this.getClass().getName(), beanXML));
		logger.info(String.format("[%s] Connector bean [%s]",this.getClass().getName(), beanID));
	}
	
	@Override
	public void buildHeaders() {
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_XML);
		
		logger.info(String.format("[%s] header content type [%s]",this.getClass().getName(), headers.getContentType().toString()));
	}

	@Override
	public void buildBodyRequest(String body,Map<String,Object> map) throws MapExpressionEvaluationException{
		
		logger.info(String.format("[%s] Start evaluation of XML request body ..",this.getClass().getName()));
		this.xmlBody = LanguageExpressionService.evaluateMapExpressionToObject(map, body);
		
		Map<String,Object> viewMap = map;
		//viewMap.put("password", "***");
		
		String requestBody = LanguageExpressionService.evaluateMapExpressionToObject(viewMap, body).toString();
				
		logger.info(String.format("[%s] XML request body:\n ",this.getClass().getName()));
		logger.info(requestBody);
		
	}

	@Override
	public String request(String method) throws InvalidHttpRequestMethod, FailedRequestException {
		
		
		switch(method) {
		
		case "GET":
			
			logger.warn(String.format("Request method GET is invalid for [%s]", this.getClass().getName()));
			logger.error(String.format("[%s] will stop and thow exception ..", this.getClass().getName()));
			
			throw new InvalidHttpRequestMethod("GET is not supported for TCS XML Requests");
			
			
		case "POST":
			
			try {
				
				HttpEntity<String> request = new HttpEntity<String>(this.xmlBody.toString(), headers);
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters()
				        .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
				
				logger.info(String.format("[%s] Will start http request .. ",this.getClass().getName()));
				ResponseEntity<String> response = restTemplate.postForEntity(connector.getUrl(), request, String.class);
			    
				logger.info(String.format("[%s] sending back response .. ",this.getClass().getName()));
				logger.info(response.getBody());
				
				return response.getBody();
			}
			catch(Exception e) {
				
				logger.warn(String.format("[%s] Unexpected error .. ",this.getClass().getName()));
				logger.error(e.getMessage());
				
				e.printStackTrace();
				
				throw new FailedRequestException("FailedRequestException: Request has failed");
			}
			
			
			default:
				logger.warn(String.format("[%s] Unknown request method .. ",this.getClass().getName()));
				logger.error(String.format("[%s] will stop and thow exception ..", this.getClass().getName()));
				
				throw new InvalidHttpRequestMethod("Unknown Request Method");
			
		}
		
	}

}
