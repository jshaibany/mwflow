package com.example.one_mw.schema.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.one_mw.entity.Service;
import com.example.one_mw.entity.ServiceBulkProcessingSafe;
import com.example.one_mw.entity.ServiceBulkProcessingSchedule;
import com.example.one_mw.entity.ServiceBulkRequest;
import com.example.one_mw.entity.ServiceBulkRequestLine;
import com.example.one_mw.entity.ServiceRequest;
import com.example.one_mw.entity.ServiceRequestLine;
import com.example.one_mw.exception.MapExpressionEvaluationException;
import com.example.one_mw.security.DecryptionService;
import com.example.one_mw.service.LanguageExpressionService;
import com.example.one_mw.service.ServiceRequestService;
import com.example.one_mw.service.ServiceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapToJsonConverter {

	public String convert(Map<String,Object> map) {
		
		try {
			return new ObjectMapper().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String prepareJsonRequest(ServiceBulkRequest serviceRequest,
			List<ServiceBulkRequestLine> serviceRequestLine,
			ServiceBulkProcessingSchedule schedule,
			ServiceService service,
			ServiceRequestService serviceRequestService) throws MapExpressionEvaluationException{
		
		//TODO to get the user credentials somehow for each request
		
		Map<String,Object> map = new HashMap<>();
		
		Service svc = service.getServiceByCode(serviceRequest.getServiceBulkConfig().getReferenceServiceCode());
		
		String jsonTemplate= svc.getJsonTemplate();
		
		for(ServiceBulkRequestLine line : serviceRequestLine) {
			
			map.put(line.getParamName(), line.getParamValue());
		}
		
		ServiceBulkProcessingSafe safe = serviceRequestService.getBulkProcessingSafeByScheduleId(schedule.getId());
		
		map.put("redirect_flag", true);
		map.put("service_code", svc.getCode());
		map.put("username", safe.getUsername());
		map.put("password", DecryptionService.decrypt(safe.getPassword()));
		map.put("user_id", safe.getUserId());
		map.put("terminal_type", safe.getTerminalType());
		
		String jsonRequest = LanguageExpressionService.evaluateMapExpressionToObject(map, jsonTemplate).toString();
		
		if(jsonRequest.isEmpty())
			throw new MapExpressionEvaluationException("MapExpressionEvaluationException");
		return jsonRequest;
	}

	public static String prepareJsonRequest(ServiceRequest serviceRequest,
			Map<String,Object> request,
			ServiceService service) throws MapExpressionEvaluationException{
		
		Map<String,Object> map = new HashMap<>();
		
		Service svc = service.getServiceByCode(serviceRequest.getServiceName());
		String jsonTemplate= svc.getJsonTemplate();
		
		for(ServiceRequestLine line : serviceRequest.getRequestLines()) {
			
			map.put(line.getParamName(), line.getParamValue());
		}
		
		map.put("redirect_flag", true);
		map.put("username", request.get("username"));
		map.put("password", request.get("password"));
		map.put("user_id", request.get("user_id"));
		map.put("terminal_type", request.get("terminal_type"));
		
		String jsonRequest = LanguageExpressionService.evaluateMapExpressionToObject(map, jsonTemplate).toString();
		
		if(jsonRequest.isEmpty())
			throw new MapExpressionEvaluationException("MapExpressionEvaluationException");
		return jsonRequest;
		
	}

}
