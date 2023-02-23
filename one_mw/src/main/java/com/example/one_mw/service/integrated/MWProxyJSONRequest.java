package com.example.one_mw.service.integrated;

import java.util.Arrays;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.one_mw.exception.FailedRequestException;
import com.example.one_mw.exception.InvalidHttpRequestMethod;
import com.example.one_mw.exception.MapExpressionEvaluationException;
import com.example.one_mw.service.LanguageExpressionService;

public class MWProxyJSONRequest implements IHttpObjRequestable{

	private HttpHeaders headers;
	private String jsonBody;
	private ICredentialsConnector connector;
	private ApplicationContext context;
	
	@Override
	public void buildContext(String beanXML, String beanID) {
		
		context 
	      = new ClassPathXmlApplicationContext(beanXML);
		connector = (ICredentialsConnector) context.getBean(beanID);
		
	}
	
	@Override
	public void buildHeaders() {
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	}
	
	@Override
	public void buildBodyRequest(String body,Map<String,Object> map) throws MapExpressionEvaluationException{
		
		
		//map.put("token", connector.getUserName());
		map.put("passcode", connector.getPassword());
		
		this.jsonBody = LanguageExpressionService.evaluateMapExpressionToObject(map, body).toString();
		
	}
	
	@Override
	public String request(String method) throws InvalidHttpRequestMethod, FailedRequestException {
		
		switch(method) {
		
		case "GET":
			
			
			throw new InvalidHttpRequestMethod("GET is not supported for MW JSON Requests");
			
			
		case "POST":
			
			try {
				
				HttpEntity<String> request = new HttpEntity<String>(this.jsonBody, headers);
				ResponseEntity<String> response = new RestTemplate().exchange(connector.getUrl(),HttpMethod.POST, request, String.class);
				
				MediaType contentType = response.getHeaders().getContentType();
				HttpStatus statusCode = response.getStatusCode();
			    
				System.out.println(contentType.toString()+statusCode.toString());
			    
				return response.getBody();
			}
			catch(Exception e) {
				
				e.printStackTrace();
				
				throw new FailedRequestException("FailedRequestException: Request has failed");
			}
			
			
			default:
				throw new InvalidHttpRequestMethod("Unknown Request Method");
			
		}
	}
}
