package com.example.one_mw.controller;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.one_mw.entity.ServiceBulkProcessingSafe;
import com.example.one_mw.entity.ServiceBulkProcessingSchedule;
import com.example.one_mw.entity.ServiceBulkRequest;
import com.example.one_mw.entity.ServiceBulkRequestChecker;
import com.example.one_mw.entity.ServiceRequest;
import com.example.one_mw.entity.ServiceRequestChecker;

import com.example.one_mw.exception.MapExpressionEvaluationException;
import com.example.one_mw.schema.converters.JsonToMapConverter;
import com.example.one_mw.schema.converters.MapToJsonConverter;
import com.example.one_mw.security.EncryptionService;
import com.example.one_mw.service.OneMwEndpointsService;
import com.example.one_mw.service.OneMwServiceInitializer;
import com.example.one_mw.service.ServiceRequestService;
import com.example.one_mw.service.ServiceService;

@RestController
public class ApprovalManagementController {
	
	Logger logger = LogManager.getLogger(ApprovalManagementController.class);

	private final ServiceRequestService serviceRequestService;
	
	private final ServiceService service;
	
	private final OneMwServiceInitializer oneMwServiceInitializer;
	
	private final OneMwEndpointsService oneMwEndpointsService;
	
	@Autowired
	public ApprovalManagementController(ServiceRequestService serviceRequestService, 
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
    value = "/v1/approveRequest")
	@ResponseBody
	public Map<String,Object> approveRequest(@RequestBody Map<String,Object> request){
		
		Map<String,Object> errorMap=new HashMap<>();
		Map<String,Object> successMap=new HashMap<>();
		/*
		 * 1. Update the Service Request, put Current Checker=NULL, Last Check By=username & Updated On (Timestamp)
		 * 2. Update the Service Request Checker, put Checker Action=A,Is Current=0 & Updated On (Timestamp)
		 * 3. Try to find Next Approver (By Request ID WHERE Is Current IS NULL) get MIN ID
		 * 		-If found, update Service Request, put Current Checker=Found Next Approver, & update Service Request Checker put Is Current=1
		 * 		-If not found, update Service Request, put Status=A (The Request is Approved and need to be processed)
		 * 4. Get the Request stored params and replace the (username,password) params by the approver username & password, then submitRequest
		 */
		
		try {
			
			logger.info(String.format("Request [%s] approval is started ..", request.get("request_id").toString()));
			
			ServiceRequest serviceRequest = serviceRequestService.getServiceRequestById(BigInteger.valueOf(Integer.valueOf(request.get("request_id").toString())));
			
			//BigInteger id=serviceRequest.getId();
			String status=serviceRequest.getStatus();
			
			List<ServiceRequestChecker> serviceRequestCheckers = serviceRequestService.getRequestCheckersByRequestId(serviceRequest.getId());
			
			/*
			 * 1. Update the Service Request, put Current Checker=NULL, Last Check By=username & Updated On (Timestamp)
			 */
			if(status.equals("P")) {
				
				serviceRequest.setNextChecker(null);
				serviceRequest.setLastCheckedBy(request.get("username").toString());
				serviceRequest.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
				
				serviceRequestService.updateServiceRequest(serviceRequest);
				
				logger.info(String.format("Request [%s] status is updated ..", request.get("request_id").toString()));
			}
			else {
				
				//In case service request is not found:
				logger.error(String.format("Request [%s] is not found or already processed ..", request.get("request_id").toString()));
				
				errorMap.put("result",-1);
				errorMap.put("message","No Result or Request is already processed: Please call your system admin");
				return errorMap;
			}
			
			/*
			 * 2. Update the Service Request Checker, put Checker Action=A,Is Current=0 & Updated On (Timestamp)
			 */
			
			
			
			serviceRequestCheckers.stream().forEach(c->{
				
				if(c.getIsCurrentChecker()!=null && c.getIsCurrentChecker()) {
					
					c.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
					c.setIsCurrentChecker(false);
					c.setCheckerAction("A");
					c.setUpdatedBy(request.get("username").toString());
					
					serviceRequestService.updateRequestChecker(c);
				}
				
			});
			
			logger.info(String.format("Request [%s] checkers are updated ..", request.get("request_id").toString()));
			
			/*
			 * 3. Try to find Next Approver (By Request ID WHERE Is Current IS NULL) get MIN ID
			 * 		-If found, update Service Request, put Current Checker=Found Next Approver, & update Service Request Checker put Is Current=1
			 * 		-If not found, update Service Request, put Status=A (The Request is Approved and need to be processed)
			 */
			
			logger.info(String.format("Request [%s] trying to find next approver ..", request.get("request_id").toString()));
			
			ServiceRequestChecker serviceRequestChecker=serviceRequestService.getNextApprover(serviceRequest.getId());
			
			if(serviceRequestChecker!=null) {
				
				logger.info(String.format("Request [%s] next approver is found ..", request.get("request_id").toString()));
				
				serviceRequest.setNextChecker(serviceRequestChecker.getRequestCheckerPrivilege());
				serviceRequestChecker.setIsCurrentChecker(true);
				
				serviceRequestService.updateServiceRequest(serviceRequest);
				serviceRequestService.updateRequestChecker(serviceRequestChecker);
				
				logger.info(String.format("Request [%s] is updated ..", request.get("request_id").toString()));
				
				successMap.put("result",0);
				successMap.put("message",String.format("Request No:%d is approved successfully", serviceRequest.getId()));
				return successMap;
				
			}
			else {
				
				logger.info(String.format("Request [%s] has no next approver ..", request.get("request_id").toString()));
				logger.info(String.format("Request [%s] trying to process the request ..", request.get("request_id").toString()));
				
				/*
				 * 4. Get the Request stored params and replace the (username,password) params by the approver username & password, then submitRequest
				 */
				try {
					
					String jsonRequest = MapToJsonConverter.prepareJsonRequest(serviceRequest,request,service);
					String result = oneMwEndpointsService.submitRequest(jsonRequest,
							oneMwServiceInitializer.getOneMwUrl(),
							oneMwServiceInitializer.getOneMwEndpointSubmitRequest());
					
					successMap= JsonToMapConverter.convertJsonToMap(result);
					
					logger.info(String.format("Request [%s] is processed successfully ..", request.get("request_id").toString()));
					
					//Upadate the request as Approved if request is submitted successfully
					serviceRequest.setStatus("A");
					serviceRequestService.updateServiceRequest(serviceRequest);
					
					logger.info(String.format("Request [%s] status is changed to [A] Approved ..", request.get("request_id").toString()));
					
					return successMap;
				}
				catch(MapExpressionEvaluationException e) {
					
					e.printStackTrace();
					
					logger.error(String.format("Request [%s] caused an error ..", request.get("request_id").toString()));
					logger.error(e.getMessage());
					
					errorMap.put("result",-1);
					errorMap.put("message",String.format("Error in processing request No:%d", serviceRequest.getId()));
					return errorMap;
				}
			}
			
			
			
		}
		catch(Exception e) {
			
			logger.error(String.format("Request [%s] caused an error ..", request.get("request_id").toString()));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			
			errorMap.put("result",-1);
			errorMap.put("message","Exception");
			return errorMap;
			
		}
		
		
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    method = RequestMethod.POST,
		    value = "/v1/approveBulkRequest")
			@ResponseBody
	public Map<String,Object> approveBulkRequest(@RequestBody Map<String,Object> request){
		
		Map<String,Object> errorMap=new HashMap<>();
		Map<String,Object> successMap=new HashMap<>();
		/*
		 * 1. Update the Service Request, put Current Checker=NULL, Last Check By=username & Updated On (Timestamp)
		 * 2. Update the Service Request Checker, put Checker Action=A,Is Current=0 & Updated On (Timestamp)
		 * 3. Try to find Next Approver (By Request ID WHERE Is Current IS NULL) get MIN ID
		 * 		-If found, update Service Request, put Current Checker=Found Next Approver, & update Service Request Checker put Is Current=1
		 * 		-If not found, update Service Request, put Status=A (The Request is Approved and need to be processed)
		 * 4. Get the Request stored params and replace the (username,password) params by the approver username & password, then submitRequest
		 */
		
		try {
			
			logger.info(String.format("Bulk Request [%s] approval is started ..", request.get("request_id").toString()));
			
			ServiceBulkRequest serviceRequest = serviceRequestService.getServiceBulkRequestById(BigInteger.valueOf(Integer.valueOf(request.get("request_id").toString())));
			
			//BigInteger id=serviceRequest.getId();
			String status=serviceRequest.getStatus();
			
			List<ServiceBulkRequestChecker> serviceRequestCheckers = serviceRequestService.getBulkRequestCheckersByRequestId(serviceRequest.getId());
			
			/*
			 * 1. Update the Service Request, put Current Checker=NULL, Last Check By=username & Updated On (Timestamp)
			 */
			if(status.equals("P")) {
				
				/*
				 * Ignore setNextChecker(null) to keep the last checked by and leave the value change for the next step
				 */
				//serviceRequest.setNextChecker(null);
				serviceRequest.setLastCheckedBy(request.get("username").toString());
				serviceRequest.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
				
				serviceRequestService.updateServiceRequest(serviceRequest);
				
				logger.info(String.format("Bulk Request [%s] status is updated ..", request.get("request_id").toString()));
			}
			else {
				
				//In case service request is not found:
				logger.error(String.format("Bulk Request [%s] is not found or already processed ..", request.get("request_id").toString()));
				
				errorMap.put("result",-1);
				errorMap.put("message","No Result or Request is already processed: Please call your system admin");
				return errorMap;
			}
			
			/*
			 * 2. Update the Service Request Checker, put Checker Action=A,Is Current=0 & Updated On (Timestamp)
			 */
			
			
			
			serviceRequestCheckers.stream().forEach(c->{
				
				if(c.getIsCurrentChecker()!=null && c.getIsCurrentChecker()) {
					
					c.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
					c.setIsCurrentChecker(false);
					c.setCheckerAction("A");
					c.setUpdatedBy(request.get("username").toString());
					
					serviceRequestService.updateRequestChecker(c);
				}
				
			});
			
			logger.info(String.format("Bulk Request [%s] checkers are updated ..", request.get("request_id").toString()));
			
			/*
			 * 3. Try to find Next Approver (By Request ID WHERE Is Current IS NULL) get MIN ID
			 * 		-If found, update Service Request, put Current Checker=Found Next Approver, & update Service Request Checker put Is Current=1
			 * 		-If not found, update Service Request, put Status=A (The Request is Approved and need to be processed)
			 */
			
			logger.info(String.format("Bulk Request [%s] trying to find next approver ..", request.get("request_id").toString()));
			
			ServiceBulkRequestChecker serviceRequestChecker=serviceRequestService.getBulkNextApprover(serviceRequest.getId());
			
			if(serviceRequestChecker!=null) {
				
				logger.info(String.format("Bulk Request [%s] next approver is found ..", request.get("request_id").toString()));
				
				serviceRequest.setNextChecker(serviceRequestChecker.getRequestCheckerPrivilege());
				serviceRequestChecker.setIsCurrentChecker(true);
				
				serviceRequestService.updateServiceRequest(serviceRequest);
				serviceRequestService.updateRequestChecker(serviceRequestChecker);
				
				logger.info(String.format("Bulk Request [%s] is updated ..", request.get("request_id").toString()));
				
				successMap.put("result",0);
				successMap.put("message",String.format("Request No:%d is approved successfully", serviceRequest.getId()));
				return successMap;
				
			}
			else {
				
				logger.info(String.format("Bulk Request [%s] has no next approver ..", request.get("request_id").toString()));
				logger.info(String.format("Bulk Request [%s] is saved for later processing ..", request.get("request_id").toString()));
				
				serviceRequest.setStatus("A");
				serviceRequest.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
				serviceRequestService.updateServiceRequest(serviceRequest);
				
				successMap.put("result",0);
				successMap.put("message",String.format("Request No:%d is approved successfully and ready for processing", serviceRequest.getId()));
				return successMap;
				
			}
			
			
			
		}
		catch(Exception e) {
			
			logger.error(String.format("Bulk Request [%s] caused an error ..", request.get("request_id").toString()));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			
			errorMap.put("result",-1);
			errorMap.put("message","Exception");
			return errorMap;
			
		}
		
		
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    method = RequestMethod.POST,
		    value = "/v1/scheduleBulkRequestProcessing")
			@ResponseBody
	public Map<String,Object> scheduleBulkRequestProcessing(@RequestBody Map<String,Object> request){
		
		Map<String,Object> errorMap=new HashMap<>();
		Map<String,Object> successMap=new HashMap<>();
		
		try {
			
			/*
			 * 1- Check if the request is Approved and not scheduled before
			 * 2- Check the provided schedule if null or empty get the now() timestamp
			 * 3- If schedule provided check if timestamp is valid, try to cast, if error reject the process
			 */
			
			logger.info(String.format("Check if Bulk Request [%s] is approved and not scheduled before ..", request.get("request_id").toString()));
			
			ServiceBulkRequest serviceRequest = serviceRequestService.getServiceBulkRequestById(BigInteger.valueOf(Integer.valueOf(request.get("request_id").toString())));
			
			String status=serviceRequest.getStatus();
				
			ServiceBulkProcessingSchedule s = serviceRequestService
					.getBulkProcessingScheduleByRequestId(BigInteger.valueOf(Integer.valueOf(request.get("request_id").toString())));
			
			if(s != null && s.getId() != null) {
				
				//Schedule is found
				logger.error(String.format("Bulk Request [%s] is scheduled before ..", request.get("request_id").toString()));
				
				errorMap.put("result",-1);
				errorMap.put("message",String.format("Bulk Request [%s] is scheduled before ..", request.get("request_id").toString()));
				return errorMap;
			}
			
			logger.info(String.format("Bulk Request [%s] schedule processing is started ..", request.get("request_id").toString()));
			
			Timestamp sch_on=null;
			
			if(status.equals("A")) {
				
				if(request.get("scheduled_on") != null) {
					
					if(request.get("scheduled_on").toString().isEmpty()) {
						
						sch_on = Timestamp.valueOf(LocalDateTime.now());
					}
					else {
						
						try {
							
							//Try to cast the provided datetime
							sch_on = Timestamp.valueOf(request.get("scheduled_on").toString());
						}
						catch(Exception e) {
							
							logger.error(String.format("Bulk Request [%s] is scheduled before ..", request.get("request_id").toString()));
							
							errorMap.put("result",-1);
							errorMap.put("message",String.format("Provided schedule time is invalid .."));
							return errorMap;
						}
					}
				}
				else {
					
					sch_on = Timestamp.valueOf(LocalDateTime.now());
				}
				
				if(sch_on != null) {
					
					try {
						
						logger.info(String.format("Bulk Request [%s] insert schedule for immediate processing ..", request.get("request_id").toString()));
						
						ServiceBulkProcessingSchedule schedule = new ServiceBulkProcessingSchedule();
						ServiceBulkProcessingSafe safe = new ServiceBulkProcessingSafe();
						
						schedule.setScheduledOn(sch_on);
						schedule.setScheduledBy(request.get("username").toString());
						schedule.setHeaderId(BigInteger.valueOf(Long.valueOf(request.get("request_id").toString())));
						schedule.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
						schedule.setStatus("P");
						
						serviceRequestService.createNewBulkProcessingSchedule(schedule);
						
						logger.info(String.format("Schedule Request [%s] is created successfully ..", schedule.getId().toString()));
						
						safe.setScheduleId(schedule.getId());
						safe.setUsername(request.get("username").toString());
						safe.setPassword(EncryptionService.encrypt(request.get("password").toString()));
						safe.setUserId(request.get("user_id").toString());
						safe.setTerminalType(request.get("terminal_type").toString());
						
						serviceRequestService.createNewBulkProcessingSafe(safe);
						
						logger.info(String.format("Safe Record [%s] is created successfully ..", safe.getId().toString()));

						successMap.put("Result", 0);
						successMap.put("Message", "Success");
						successMap.put("Schedule", schedule);
						
						return successMap;
					}
					catch(Exception e) {
						
						logger.error(String.format("Bulk Request [%s] caused an error ..", request.get("request_id").toString()));
						logger.error(e.getMessage());
						
						errorMap.put("result",-1);
						errorMap.put("message",String.format("Error in processing bulk request No:%d", serviceRequest.getId()));
						return errorMap;
					}
				}
				else {
					
					logger.error(String.format("Bulk Request [%s] is scheduled before ..", request.get("request_id").toString()));
					
					errorMap.put("result",-1);
					errorMap.put("message",String.format("Provided schedule time is invalid .."));
					return errorMap;
				}
				
			}
			else {
				
				//In case service request is not Approved:
				errorMap.put("result",-1);
				errorMap.put("message","This request is not approved yet.");
				return errorMap;
			}
		}
		catch(Exception e) {
			
			logger.error(String.format("Bulk Request [%s] schedule creation caused an error ..", request.get("request_id").toString()));
			
			errorMap.put("result",-1);
			errorMap.put("message",String.format("Bulk Request [%s] schedule creation caused an error ..", request.get("request_id").toString()));
			return errorMap;
		}
		
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
    method = RequestMethod.POST,
    value = "/v1/rejectRequest")
	@ResponseBody
	public Map<String,Object> rejectRequest(@RequestBody Map<String,Object> request){
		
		Map<String,Object> errorMap=new HashMap<>();
		Map<String,Object> successMap=new HashMap<>();
		
		/*
		 * 1. Update the Service Request, put Status=R, Current Checker=NULL, Last Check By=username & Updated On (Timestamp)
		 * 2. Update the Service Request Checker, put Checker Action=R,Is Current=0 & Updated On (Timestamp)
		 */
		
		logger.info(String.format("Request [%s] reject is started ..", request.get("request_id").toString()));
		
		try {
			
			ServiceRequest serviceRequest = serviceRequestService.getServiceRequestById((BigInteger) request.get("request_id"));
			
			//BigInteger id=serviceRequest.getId();
			String status=serviceRequest.getStatus();
			
			List<ServiceRequestChecker> serviceRequestCheckers = serviceRequestService.getRequestCheckersByRequestId(serviceRequest.getId());
			
			/*
			 * 1. Update the Service Request, put Status=R, Current Checker=NULL, Last Check By & Updated On (Date)
		
			 */
			if(status.equals("P")) {
				
				serviceRequest.setStatus("R");
				serviceRequest.setNextChecker(null);
				serviceRequest.setLastCheckedBy(request.get("username").toString());
				serviceRequest.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
				
				serviceRequestService.updateServiceRequest(serviceRequest);
				
				logger.info(String.format("Request [%s] status is updated ..", request.get("request_id").toString()));
			}
			else {
				
				//In case service request is not found:
				logger.error(String.format("Request [%s] is not found or already processed ..", request.get("request_id").toString()));
				
				errorMap.put("result",-1);
				errorMap.put("message","No Result or Request is already processed: Please call your system admin");
				return errorMap;
			}
			
			/*
			 * 2. Update the Service Request Checker, put Checker Action=R,Is Current=0 & Updated On (Date)
			 */
			
			
			
			serviceRequestCheckers.stream().forEach(c->{
				
				c.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
				c.setIsCurrentChecker(false);
				c.setCheckerAction("R");
				c.setUpdatedBy(request.get("username").toString());
				
				serviceRequestService.updateRequestChecker(c);
				
			});	
			
			logger.info(String.format("Request [%s] checkers are updated ..", request.get("request_id").toString()));
			
			successMap.put("result",-1);
			successMap.put("result",String.format("Request No:%d is rejected successfully", serviceRequest.getId()));
			
			return successMap;
			
		}
		catch(Exception e) {
			
			logger.error(String.format("Request [%s] caused an error ..", request.get("request_id").toString()));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			errorMap.put("result",-1);
			errorMap.put("message","Exception");
			return errorMap;
			
		}
		
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    method = RequestMethod.POST,
		    value = "/v1/rejectBulkRequest")
			@ResponseBody
	public Map<String,Object> rejectBulkRequest(@RequestBody Map<String,Object> request){
		
		Map<String,Object> errorMap=new HashMap<>();
		Map<String,Object> successMap=new HashMap<>();
		
		/*
		 * 1. Update the Service Request, put Status=R, Current Checker=NULL, Last Check By=username & Updated On (Timestamp)
		 * 2. Update the Service Request Checker, put Checker Action=R,Is Current=0 & Updated On (Timestamp)
		 */
		
		logger.info(String.format("Bulk Request [%s] reject is started ..", request.get("request_id").toString()));
		
		try {
			
			ServiceBulkRequest serviceRequest = serviceRequestService.getServiceBulkRequestById((BigInteger) request.get("request_id"));
			
			//BigInteger id=serviceRequest.getId();
			String status=serviceRequest.getStatus();
			
			List<ServiceBulkRequestChecker> serviceRequestCheckers = serviceRequestService.getBulkRequestCheckersByRequestId(serviceRequest.getId());
			
			/*
			 * 1. Update the Service Request, put Status=R, Current Checker=NULL, Last Check By & Updated On (Date)
		
			 */
			if(status.equals("P")) {
				
				serviceRequest.setStatus("R");
				serviceRequest.setNextChecker(null);
				serviceRequest.setLastCheckedBy(request.get("username").toString());
				serviceRequest.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
				
				serviceRequestService.updateServiceRequest(serviceRequest);
				
				logger.info(String.format("Bulk Request [%s] status is updated ..", request.get("request_id").toString()));
			}
			else {
				
				//In case service request is not found:
				logger.error(String.format("Bulk Request [%s] is not found or already processed ..", request.get("request_id").toString()));
				
				errorMap.put("result",-1);
				errorMap.put("message","No Result or Request is already processed: Please call your system admin");
				return errorMap;
			}
			
			/*
			 * 2. Update the Service Request Checker, put Checker Action=R,Is Current=0 & Updated On (Date)
			 */
			
			
			
			serviceRequestCheckers.stream().forEach(c->{
				
				c.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
				c.setIsCurrentChecker(false);
				c.setCheckerAction("R");
				c.setUpdatedBy(request.get("username").toString());
				
				serviceRequestService.updateRequestChecker(c);
				
			});	
			
			logger.info(String.format("Bulk Request [%s] checkers are updated ..", request.get("request_id").toString()));
			
			successMap.put("result",-1);
			successMap.put("result",String.format("Request No:%d is rejected successfully", serviceRequest.getId()));
			
			return successMap;
			
		}
		catch(Exception e) {
			
			logger.error(String.format("Bulk Request [%s] caused an error ..", request.get("request_id").toString()));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			errorMap.put("result",-1);
			errorMap.put("message","Exception");
			return errorMap;
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/v1/getPendingRequests")
	@ResponseBody
	public Map<String,Object> getPendingRequests(@RequestBody Map<String,Object> jsonRequest){
		
		Map<String,Object> privileges = (Map<String,Object>) jsonRequest.get("Lines");
		String accountId = (String) jsonRequest.get("account_id");
		
		return serviceRequestService.getPendingApprovalRequests(accountId, privileges);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/v1/getPendingBulkRequests")
	@ResponseBody
	public Map<String,Object> getPendingBulkRequests(@RequestBody Map<String,Object> jsonRequest){
		
		Map<String,Object> privileges = (Map<String,Object>) jsonRequest.get("Lines");
		String accountId = (String) jsonRequest.get("account_id");
		
		return serviceRequestService.getPendingApprovalBulkRequests(accountId, privileges);
	}

}
