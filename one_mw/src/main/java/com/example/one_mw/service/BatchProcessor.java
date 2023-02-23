package com.example.one_mw.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.one_mw.entity.ServiceBulkConfig;
import com.example.one_mw.entity.ServiceBulkProcessingReport;
import com.example.one_mw.entity.ServiceBulkProcessingSchedule;
import com.example.one_mw.entity.ServiceBulkRequest;
import com.example.one_mw.entity.ServiceBulkRequestLine;
import com.example.one_mw.schema.converters.JsonToMapConverter;
import com.example.one_mw.schema.converters.MapToJsonConverter;

@Service
public class BatchProcessor {

	Logger logger = LogManager.getLogger(BatchProcessor.class);

	private final ServiceRequestService serviceRequestService;
	
	private final ServiceService service;
	
	private final OneMwServiceInitializer oneMwServiceInitializer;
	
	private final OneMwEndpointsService oneMwEndpointsService;
	
	@Autowired
	public BatchProcessor(ServiceRequestService serviceRequestService, 
			ServiceService service,
			OneMwServiceInitializer oneMwServiceInitializer, 
			OneMwEndpointsService oneMwEndpointsService) {
		super();
		this.serviceRequestService = serviceRequestService;
		this.service = service;
		this.oneMwServiceInitializer = oneMwServiceInitializer;
		this.oneMwEndpointsService = oneMwEndpointsService;
	}
	
	@Transactional
	public List<ServiceBulkProcessingSchedule> lockBulkSchedules() {
		
		logger.info(String.format("Scheduled Batch Processing trying to get pending schedules .."));
		
		List<ServiceBulkProcessingSchedule> schedules = serviceRequestService.getPendingBulkProcessingSchedules();
		
		if(schedules.size() <= 0) {
			
			logger.info(String.format("Scheduled Batch Processing found no schedules .."));
			
			return null;
		}
		
		logger.info(String.format("Scheduled Batch Processing found [%d] schedules ..",schedules.size()));
		
		try {
			
			for(ServiceBulkProcessingSchedule schedule : schedules) {
				
				logger.info(String.format("Scheduled Batch Processing update found schedules status to [PR] pending run .."));
				
				schedule.setStatus("PR"); //Pending Run
				
				serviceRequestService.updateBulkProcessingSchedule(schedule);
				
				logger.info(String.format("Scheduled Batch Processing schedules status updated successfully.."));
			}
			
			return schedules;
		}
		catch(Exception e) {
			
			logger.error(String.format("Scheduled Batch Processing caused an error while trying to lock schedules for processing .."));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional
	public Map<String,Object> processSchedules(List<ServiceBulkProcessingSchedule> schedules){
		
		Map<String,Object> errorMap=new HashMap<>();
		Map<String,Object> successMap=new HashMap<>();

		try {
			
			
			for(ServiceBulkProcessingSchedule schedule : schedules) {
				
				logger.info(String.format("Scheduled Batch Processing starting to process schedule [%d] ..",schedule.getId()));
				logger.info(String.format("Scheduled Batch Processing trying to get Service Bulk Request [%d] ..",schedule.getHeaderId()));
				
				ServiceBulkRequest serviceRequest = serviceRequestService.getServiceBulkRequestById(schedule.getHeaderId());
				
				List<ServiceBulkConfig> serviceBulkConfig = serviceRequestService.getServiceBulkConfig(serviceRequest.getServiceId());
				
				serviceRequest.setServiceBulkConfig(serviceBulkConfig.get(0));
				
				logger.info(String.format("Scheduled Batch Processing trying to get Service Bulk Request [%d] line numbers ..",schedule.getHeaderId()));
				
				List<Integer> bulkRequestLineNumbers = serviceRequestService.getBulkRequestLineNumbers(serviceRequest.getId());
				
				logger.info(String.format("Scheduled Batch Processing, Service Bulk Request [%d] lines found [%d] ..",
						schedule.getHeaderId(),
						bulkRequestLineNumbers.size()));
				
				
				List<Map<String,Object>> outMaps= new ArrayList<>();
				
				schedule.setActualRuntimeStart(Timestamp.valueOf(LocalDateTime.now()));
				
				for(Integer l : bulkRequestLineNumbers) {
					
					logger.info(String.format("Scheduled Batch Processing, Start Service Bulk Request [%d] line [%d] ..",
							schedule.getHeaderId(),
							l));
					
					List<ServiceBulkRequestLine> requestLines = serviceRequestService.getBulkLinesByRequestIdAndLineNumber(serviceRequest.getId(), l);
					
					String json = MapToJsonConverter.prepareJsonRequest(serviceRequest, requestLines, schedule,service,serviceRequestService);

					String result = oneMwEndpointsService.submitRequest(json,
							oneMwServiceInitializer.getOneMwUrl(),
							oneMwServiceInitializer.getOneMwEndpointSubmitRequest());			
					
					Map<String,Object> outMap = JsonToMapConverter.convertJsonToMap(result);
					Integer processResult =Integer.valueOf(outMap.get("result").toString());
					
					ServiceBulkProcessingReport reportLine = new ServiceBulkProcessingReport();
					
					reportLine.setHeaderId(schedule.getHeaderId());
					reportLine.setLineNumber(l);
					
					if(processResult == 0) {
						
						//Process got a result from TCS but it could be a failed response
						
						@SuppressWarnings("unchecked")
						Map<String,Object> processOut = (Map<String,Object>) outMap.get("processOutput");
						
						reportLine.setMessage(processOut.get("Message").toString());
						reportLine.setResult(Integer.valueOf(processOut.get("Result").toString()));
					}
					else {
						
						reportLine.setMessage(outMap.get("message").toString());
						reportLine.setResult(Integer.valueOf(outMap.get("result").toString()));
					}
					
					
					serviceRequestService.createNewBulkProcessingReport(reportLine);
					
					logger.info(String.format("Scheduled Batch Processing, Service Bulk Request [%d] line [%d] is processed ..",
							schedule.getHeaderId(),
							l));
					
					
					//TODO to report each line processing result
					
					outMaps.add(outMap);
				}
				
				schedule.setActualRuntimeEnd(Timestamp.valueOf(LocalDateTime.now()));
				schedule.setStatus("C");
				
				serviceRequestService.updateBulkProcessingSchedule(schedule);
	
				logger.info(String.format("Scheduled Batch Processing, Service Bulk Request [%d] is completed ..",
						schedule.getHeaderId()));
			}
			
			successMap.put("Result", 0);
			successMap.put("Message", "Success");
			
			return successMap;
		}
		catch(Exception e) {
			
			logger.error(String.format("Scheduled Batch Processing caused an error [%s] ..", LocalDateTime.now().toString()));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			
			errorMap.put("result",-1);
			errorMap.put("message",String.format("Error in processing bulk request "));
			return errorMap;
		}
	}
}
