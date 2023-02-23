package com.example.one_mw.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.one_mw.entity.Service;
import com.example.one_mw.service.runtime.ServiceRequestManager;

@RestController
public class SecurityController {

	Logger logger = LogManager.getLogger(SecurityController.class);
	
	private final ServiceRequestManager manager;
	
	@Autowired
	public SecurityController(ServiceRequestManager manager) {
		super();
		this.manager = manager;
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
    method = RequestMethod.POST,
    value = "/v1/authorizeRequest")
	@ResponseBody
	public Map<String,Object> authorizeRequest(@RequestBody Map<String,Object> jsonRequest){
		
		Map<String,Object> response=new HashMap<>();
		response.put("result", -1);
		response.put("message", "Not Authorized");
		
		
		try {
			
			String serviceCode = jsonRequest.get("service_code").toString();
			Service service=manager.getService(serviceCode);
			
			logger.info(String.format("Try to authorize request for service [%s]", serviceCode));
			
			@SuppressWarnings("unchecked")
			Map<String,Object> privileges = (Map<String, Object>) jsonRequest.get("Lines");
			String maker = service.getServiceMaker();
			
			logger.info(String.format("Get service maker [%s] for service [%s]",maker, serviceCode));
			
			if(privileges.containsValue(maker)) {
				
				logger.info(String.format("Request is authorized for service [%s]", serviceCode));
				response.put("result", 0);
				response.put("message", "Authorized");
			}
		}
		catch(Exception e) {
			
			logger.error(String.format("Request authorization error"));
			logger.error(String.format("%s", e.getMessage()));
			
			e.printStackTrace();
			response.put("result", -1);
			response.put("message", "Not Authorized");
		}
		
		logger.info(String.format("Request is NOT authorized"));
		return response;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    		method = RequestMethod.POST,
		    		value = "/v1/authorizeRequestChecker")
	@ResponseBody
	public Map<String,Object> authorizeRequestChecker(@RequestBody Map<String,Object> jsonRequest){
		
		String passcode="kkkfjuhcvgdbasnbyvxgxbvbxnmcyufgea";
		Map<String,Object> response = new HashMap<>();
		BigInteger requestId;
		String accountId;
		Map<String,Object> privileges= new HashMap<>();
		
		/*
		 * Verify passcode, get request ID, get account_id
		 */
		
		try {
			
			if(!passcode.contentEquals(jsonRequest.get("passcode").toString())) {
				
				logger.error(String.format("Not Authorized Request"));
				
				response.put("result", -1);
				response.put("message", "Not Authorized");
				
				return response;
			}
			
			requestId = BigInteger.valueOf(Integer.valueOf(jsonRequest.get("request_id").toString()));
			accountId = jsonRequest.get("account_id").toString();
			
			privileges = (Map<String, Object>) jsonRequest.get("Lines");
			
			if(manager.authorizedRequestChecker(requestId, accountId, privileges)) {
				
				logger.error(String.format("Not Authorized Request"));
				
				response.put("result", 0);
				response.put("message", "Authorized");
				
				return response;
			}
			
			logger.error(String.format("Not Authorized"));
			
			response.put("result", -1);
			response.put("message", "Not Authorized");
			
			return response;
		}
		catch(Exception e) {
			
			e.printStackTrace();
			
			response.put("result", -1);
			response.put("message", "Not Authorized");
			
			return response;
		}
		
		/*
		 * END Verify passcode
		 */
		
		
		
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    		method = RequestMethod.POST,
		    		value = "/v1/authorizeBulkRequestChecker")
	@ResponseBody
	public Map<String,Object> authorizeBulkRequestChecker(@RequestBody Map<String,Object> jsonRequest){
		
		String passcode="kkkfjuhcvgdbasnbyvxgxbvbxnmcyufgea";
		Map<String,Object> response = new HashMap<>();
		BigInteger requestId;
		String accountId;
		Map<String,Object> privileges= new HashMap<>();
		
		/*
		 * Verify passcode, get request ID, get account_id
		 */
		
		try {
			
			if(!passcode.contentEquals(jsonRequest.get("passcode").toString())) {
				
				logger.error(String.format("Not Authorized Request"));
				
				response.put("result", -1);
				response.put("message", "Not Authorized");
				
				return response;
			}
			
			requestId = BigInteger.valueOf(Integer.valueOf(jsonRequest.get("request_id").toString()));
			accountId = jsonRequest.get("account_id").toString();
			
			privileges = (Map<String, Object>) jsonRequest.get("Lines");
			
			if(manager.authorizedBulkRequestChecker(requestId, accountId, privileges)) {
				
				logger.error(String.format("Not Authorized Request"));
				
				response.put("result", 0);
				response.put("message", "Authorized");
				
				return response;
			}
			
			logger.error(String.format("Not Authorized"));
			
			response.put("result", -1);
			response.put("message", "Not Authorized");
			
			return response;
		}
		catch(Exception e) {
			
			logger.error(String.format("Not Authorized"));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			
			response.put("result", -1);
			response.put("message", "Not Authorized");
			
			return response;
		}
		
		/*
		 * END Verify passcode
		 */
		
		
		
	}

}
