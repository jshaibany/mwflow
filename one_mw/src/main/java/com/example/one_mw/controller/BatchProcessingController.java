package com.example.one_mw.controller;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.example.one_mw.entity.ServiceBulkConfig;
import com.example.one_mw.entity.ServiceBulkProcessingSchedule;
import com.example.one_mw.entity.ServiceBulkRequest;
import com.example.one_mw.entity.ServiceBulkRequestLine;

import com.example.one_mw.schema.converters.JsonToMapConverter;
import com.example.one_mw.schema.converters.MapToJsonConverter;
import com.example.one_mw.service.OneMwEndpointsService;
import com.example.one_mw.service.OneMwServiceInitializer;
import com.example.one_mw.service.ServiceRequestService;
import com.example.one_mw.service.ServiceService;

@RestController
public class BatchProcessingController {

	Logger logger = LogManager.getLogger(BatchProcessingController.class);

	private final ServiceRequestService serviceRequestService;
	
	private final ServiceService service;
	
	private final OneMwServiceInitializer oneMwServiceInitializer;
	
	private final OneMwEndpointsService oneMwEndpointsService;
	
	@Autowired
	public BatchProcessingController(ServiceRequestService serviceRequestService, 
			ServiceService service,
			OneMwServiceInitializer oneMwServiceInitializer, 
			OneMwEndpointsService oneMwEndpointsService) {
		super();
		this.serviceRequestService = serviceRequestService;
		this.service = service;
		this.oneMwServiceInitializer = oneMwServiceInitializer;
		this.oneMwEndpointsService = oneMwEndpointsService;
	}

	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    method = RequestMethod.POST,
		    value = "/v1/startBatchProcessor")
			@ResponseBody
	public Map<String,Object> startBatchProcessor(@RequestBody Map<String,Object> jsonRequest){
		
		Map<String,Object> errorMap=new HashMap<>();
		Map<String,Object> successMap=new HashMap<>();
		
		/*
		 * 1- Get pending & approved bulk requests
		 * 2- Loop in request lines per each bulk
		 */
		List<ServiceBulkProcessingSchedule> schedules = serviceRequestService.getPendingBulkProcessingSchedules();
		
		try {
			
			for(ServiceBulkProcessingSchedule schedule : schedules) {
				
				schedule.setStatus("PR"); //Pending Run
				
				serviceRequestService.updateBulkProcessingSchedule(schedule);
			}
			
			for(ServiceBulkProcessingSchedule schedule : schedules) {
				
				ServiceBulkRequest serviceRequest = serviceRequestService.getServiceBulkRequestById(schedule.getHeaderId());
				
				List<ServiceBulkConfig> serviceBulkConfig = serviceRequestService.getServiceBulkConfig(serviceRequest.getServiceId());
				
				serviceRequest.setServiceBulkConfig(serviceBulkConfig.get(0));
				
				List<Integer> bulkRequestLineNumbers = serviceRequestService.getBulkRequestLineNumbers(serviceRequest.getId());
				
				List<Map<String,Object>> outMaps= new ArrayList<>();
				
				schedule.setActualRuntimeStart(Timestamp.valueOf(LocalDateTime.now()));
				
				for(Integer l : bulkRequestLineNumbers) {
					
					List<ServiceBulkRequestLine> requestLines = serviceRequestService.getBulkLinesByRequestIdAndLineNumber(serviceRequest.getId(), l);
					
					String json = MapToJsonConverter.prepareJsonRequest(serviceRequest, requestLines, schedule,service,serviceRequestService);

					String result = oneMwEndpointsService.submitRequest(json,
							oneMwServiceInitializer.getOneMwUrl(),
							oneMwServiceInitializer.getOneMwEndpointSubmitRequest());			
					
					Map<String,Object> outMap = JsonToMapConverter.convertJsonToMap(result);
					
					outMaps.add(outMap);
				}
				
				schedule.setActualRuntimeEnd(Timestamp.valueOf(LocalDateTime.now()));
				schedule.setStatus("C");
				
				serviceRequestService.updateBulkProcessingSchedule(schedule);
	
			}
			
			successMap.put("Result", 0);
			successMap.put("Message", "Success");
			
			return successMap;
		}
		catch(Exception e) {
			
			System.out.println("Exception");
			errorMap.put("result",-1);
			errorMap.put("message",String.format("Error in processing bulk request "));
			return errorMap;
		}
		
	}
	
}
