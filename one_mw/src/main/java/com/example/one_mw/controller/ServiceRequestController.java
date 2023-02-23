package com.example.one_mw.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.one_mw.entity.Service;
import com.example.one_mw.entity.ServiceBulkRequest;
import com.example.one_mw.entity.ServiceRequest;
import com.example.one_mw.exception.CreateApprovalListException;
import com.example.one_mw.exception.CreateServiceRequestLineException;
import com.example.one_mw.exception.FailedProcessRunException;
import com.example.one_mw.exception.FailedToFindProcessNameException;
import com.example.one_mw.exception.HttpResponseToMapConvertException;
import com.example.one_mw.exception.InvalidHttpRequestMethod;
import com.example.one_mw.exception.InvalidMapSchemaException;
import com.example.one_mw.exception.InvalidServiceException;
import com.example.one_mw.exception.InvalidServiceSchemaException;
import com.example.one_mw.exception.InvalidTaskParametersException;
import com.example.one_mw.exception.InvalidTaskSchemaException;
import com.example.one_mw.exception.MapExpressionEvaluationException;
import com.example.one_mw.exception.ProcessExpressionEvaluationException;
import com.example.one_mw.exception.TerminatedProcessRunException;
import com.example.one_mw.service.runtime.ServiceRequestManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@RestController
public class ServiceRequestController {
	
	private final ServiceRequestManager manager;
	
	Logger logger = LogManager.getLogger(ServiceRequestController.class);
	
	@Autowired
	public ServiceRequestController(ServiceRequestManager manager) {
		super();
		this.manager = manager;
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
    method = RequestMethod.POST,
    value = "/v1/submitRequestForApproval")
	@ResponseBody
	public Map<String,Object> submitRequestForApproval(@RequestBody Map<String,Object> jsonRequest){
		
		Map<String,Object> response=new HashMap<>();
		response.put("result", -1);
		
		try {
			
		
			String serviceCode = jsonRequest.get("service_code").toString();
			ServiceRequest result = manager.createNewServiceRequest(manager.getService(serviceCode), jsonRequest);
			
			
			response.put("result", 0);
			response.put("request_id", result.getId());
			
		}
		catch(CreateServiceRequestLineException e) {
			
			e.printStackTrace();
			
			response.put("result", -1);
			response.put("message", "CreateServiceRequestLineException");
		}
		catch(CreateApprovalListException e) {
			
			e.printStackTrace();
			
			response.put("result", -1);
			response.put("message", "CreateApprovalListException");
		}
		catch(Exception e) {
			
			e.printStackTrace();
			
			response.put("result", -1);
			response.put("message", "General Exception");
		}

		return response;
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    method = RequestMethod.POST,
		    value = "/v1/submitBulkRequestForApproval")
			@ResponseBody
	public Map<String,Object> submitBulkRequestForApproval(@RequestBody Map<String,Object> jsonRequest){
		
		Map<String,Object> response=new HashMap<>();
		response.put("result", -1);
		
		try {
			
			logger.info("Start submitting bulk request for approval..");
			logger.info("Validate the incoming request map ..");
			
			try {
				
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> lines = (List<Map<String, Object>>) jsonRequest.get("lines");
				
				logger.info(String.format("Total number of bulk lines found [%d]", lines.size()));
				
				String serviceCode = jsonRequest.get("service_code").toString();
				ServiceBulkRequest result = manager.createNewServiceRequest(manager.getService(serviceCode), jsonRequest,lines);
				
				response.put("result", 0);
				response.put("request_id", result.getId());
				
				return response;
			}
			catch(Exception e) {
				
				logger.error("Request format is invalid, missing bulk lines!");
				
				response.put("result", -1);
				response.put("message", "Request format is invalid, missing bulk lines!");
				
				return response;
			}
		}
		catch(Exception e) {
			
			return response;
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
					method = RequestMethod.POST,
					value = "/v1/submitRequest")
	@ResponseBody
	public Map<String,Object> submitRequest(@RequestBody Map<String,Object> jsonRequest){
		
		Map<String,Object> response=new HashMap<>();
		Map<String,Object> processOutput=new HashMap<>();
		
		response.put("result", -1);
		
		logger.info("Start submitting request { } ",LocalDateTime.now());
		
		String serviceCode;
		Service service;
		
		/*
		 * Service code validation
		 */
		
		try {
			
			serviceCode = jsonRequest.get("service_code").toString();
			
			logger.info(String.format("Service Code provided:%s", serviceCode));
			
			service = manager.getService(serviceCode);
			
		} catch (InvalidServiceException e) {
			
			e.printStackTrace();
			
			logger.error("Invalid service code");
			
			response.put("result", -1);
			response.put("message", "Invalid service code");
			
			logger.error("End submitting request { } ",LocalDateTime.now());
			
			return response;
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			logger.error("Unknown Exception, Level: Service Code Verification");
			
			response.put("result", -1);
			response.put("message", "Unknown Exception, Level: Service Code Verification");
			
			logger.error("End submitting request { } ",LocalDateTime.now());
			return response;
		}
		
		/*
		 * END Service code validation *******************************************************************************
		 */
		
		/*
		 * Service initialize & find associated process
		 */
		
		try {
			
			service = manager.initializeServiceRequest(jsonRequest, service);
			
			if(service.getProcessSchemaItems() == null || service.processName == null || service.processName.isEmpty()) {
				
				logger.error("Failed to initialize service components");
				
				response.put("result", -1);
				response.put("message", "Failed to initialize service components");
				
				logger.error("End submitting request { } ",LocalDateTime.now());
				return response;
			}
			
		} catch (InvalidMapSchemaException | InvalidServiceSchemaException | 
				MapExpressionEvaluationException | FailedToFindProcessNameException e) {
			
			e.printStackTrace();
			
			logger.error("Exception: Failed to initialize service components");
			logger.error(e.getMessage());
			
			response.put("result", -1);
			response.put("message", "Failed to initialize service components");
			
			logger.error("End submitting request { } ",LocalDateTime.now());
			return response;
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			logger.error("Unknown Exception, Level: Service Initialization");
			
			response.put("result", -1);
			response.put("message", "Unknown Exception, Level: Service Initialization");
			
			logger.error("End submitting request { } ",LocalDateTime.now());
			return response;
		}
		
		/*
		 * END Service initialize & find associated process **********************************************************
		 */
		
		logger.info(String.format("Service is found:%s", service.getName()));
		logger.info(String.format("Process associated is found:%s", service.processName));
		logger.info("Starting the process..");
		
		try {
			
			processOutput=manager.startProcess(service.processName,service);
			
			if(processOutput.get("result") != null) {
				
				if(Integer.valueOf(processOutput.get("result").toString()).intValue() == 0) {
					
					//Process has completed successfully
					try {
						
						return manager.prepareServiceOutputMap(service, (Map<String,Object>) processOutput.get("processOutput"));
						
					} catch (InvalidMapSchemaException e) {
						
						e.printStackTrace();
						
						logger.error("Exception: Invalid service output schema");
						logger.error(e.getMessage());
						
						response.put("result", -1);
						response.put("message", "Invalid service output schema");
						
						return response;
					}
				}
				else {
					
					if(Integer.valueOf(processOutput.get("result").toString()).intValue() > 0) {
						
						//Process has some failed tasks
						try {
							
							return manager.prepareServiceOutputMap(service, (Map<String,Object>) processOutput.get("processOutput"));
							
						} catch (InvalidMapSchemaException e) {
							
							e.printStackTrace();
							
							logger.error("Exception: Invalid service output schema");
							logger.error(e.getMessage());
							
							response.put("result", -1);
							response.put("message", "Invalid service output schema");
							
							return response;
						}
					}
					else {
						
						//Process has completely failed
						logger.error("Process has completely failed");
						
						response.put("result", -1);
						response.put("message", "Process has completely failed");
						
						return response;
					}
				}
				
			}
			else {
				
				logger.error("Fatal: Process returned no result object");
				
				response.put("result", -1);
				response.put("message", "Fatal: Process returned no result object");
				
				return response;
			}
			
		} catch (InvalidTaskSchemaException | MapExpressionEvaluationException | InvalidHttpRequestMethod
				| HttpResponseToMapConvertException | InvalidTaskParametersException
				| ProcessExpressionEvaluationException | FailedProcessRunException | TerminatedProcessRunException e) {
			
			e.printStackTrace();
			
			logger.error("Exception: Process run has failed");
			logger.error(e.getMessage());
			
			response.put("result", -1);
			response.put("message", "Exception: Process run has failed");
			
			return response;
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			logger.error("Unknown Exception, Level: Process Run");
			
			response.put("result", -1);
			response.put("message", "Unknown Exception, Level: Process Run");
			
			return response;
		}
		
		
		
	}

	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, 
		    method = RequestMethod.POST,
		    value = "/v1/submitBulkRequest")
			@ResponseBody
	public Map<String,Object> submitBulkRequest(@RequestBody Map<String,Object> jsonRequest){
		
		//1- Validate the input map if it contains an Object named @lines and could be casted to List<Map<String,Object>>
		//2- Validate the service in request to be Bulk Enabled
		//3- Proceed with the request processing as usual
		//4- Return the Request Number if successfully created or error message
		
		Map<String,Object> response=new HashMap<>();
		response.put("result", -1);
		
		try {
			
			logger.info("Start submitting bulk request ..");
			logger.info("Validate the incoming request map ..");
			
			try {
				
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> lines = (List<Map<String, Object>>) jsonRequest.get("lines");
				
				logger.info(String.format("Total number of bulk lines found [%d]", lines.size()));
				
			}
			catch(Exception e) {
				
				logger.error("Request format is invalid, missing bulk lines!");
				
				response.put("result", -1);
				response.put("message", "Request format is invalid, missing bulk lines!");
				
				return response;
			}
			
			String serviceCode = jsonRequest.get("service_code").toString();
			
			logger.info(String.format("Service Code provided:%s", serviceCode));
			
			Service service = manager.getService(serviceCode);
			
			//Override the schema builder
			service.setProcessSchemaItems(jsonRequest);
			
			service.processName = manager.getServiceBulkProcess(service.getId());

			if(service != null && service.processName != null) {
				
				logger.info(String.format("Service is found:%s", service.getName()));
				logger.info(String.format("Bulk Process associated is found:%s", service.processName));
				logger.info("Starting the process..");
				
				response=manager.startProcess(service.processName,service);
				
				
			}
			
			return response;
			
		}catch (InvalidServiceException e) {
			
			e.printStackTrace();
			response.put("result", -1);
			response.put("message", e.getMessage());
			
			logger.error(e.getMessage());
			
			return response;
		}catch (MapExpressionEvaluationException e) {
			
			e.printStackTrace();
			response.put("result", -1);
			response.put("message", e.getMessage());
			
			logger.error(e.getMessage());
			
			return response;
		}catch (InvalidTaskSchemaException e) {
			
			e.printStackTrace();
			response.put("result", -1);
			response.put("message", e.getMessage());
			
			logger.error(e.getMessage());
			
			return response;
		} catch (InvalidHttpRequestMethod e) {
			
			e.printStackTrace();
			response.put("result", -1);
			response.put("message", e.getMessage());
			
			logger.error(e.getMessage());
			
			return response;
		} catch (HttpResponseToMapConvertException e) {
			
			e.printStackTrace();
			response.put("result", -1);
			response.put("message", e.getMessage());
			
			logger.error(e.getMessage());
			
			return response;
		} catch (InvalidTaskParametersException e) {
			
			e.printStackTrace();
			response.put("result", -1);
			response.put("message", e.getMessage());
			
			logger.error(e.getMessage());
			
			return response;
		} catch (ProcessExpressionEvaluationException e) {
			
			e.printStackTrace();
			response.put("result", -1);
			response.put("message", e.getMessage());
			
			logger.error(e.getMessage());
			
			return response;
		} catch (FailedProcessRunException e) {
			
			e.printStackTrace();
			response.put("result", -1);
			response.put("message", e.getMessage());
			
			logger.error(e.getMessage());
			
			return response;
			
		} catch (TerminatedProcessRunException e) {

			e.printStackTrace();
			response.put("result", -1);
			response.put("message", e.getMessage());
			
			logger.error(e.getMessage());
			
			return response;
		}
	}

}
