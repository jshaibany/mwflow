package com.example.one_mw.service.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.one_mw.service.LanguageExpressionService;
import com.example.one_mw.service.SchemaService;
import com.example.one_mw.service.ServiceService;
import com.example.one_mw.entity.ISchemaItem;
import com.example.one_mw.entity.ServiceDecisionLine;
import com.example.one_mw.exception.FailedToFindProcessNameException;
import com.example.one_mw.exception.InvalidMapSchemaException;
import com.example.one_mw.exception.InvalidServiceException;
import com.example.one_mw.exception.InvalidServiceSchemaException;
import com.example.one_mw.exception.MapExpressionEvaluationException;

@Service
public class BusinessServiceManager {

	Logger logger = LogManager.getLogger(BusinessServiceManager.class);
	
	private final ServiceService businessServices;
	/*
	 * @serviceContainer is used to hold Service
	 */
	private Map<String,Object> serviceContainer;
	
	@Autowired
	public BusinessServiceManager(ServiceService businessServices) {
		super();
		this.businessServices = businessServices;
		this.serviceContainer = new HashMap<>();
	}

	private void addServiceToServiceManager(com.example.one_mw.entity.Service entity) {
		
		if(this.serviceContainer == null)
			this.serviceContainer = new HashMap<>();
		
		this.serviceContainer.put(entity.getCode(),entity);
	}
	
	public void validateServiceSchema(Map<String,Object> map,
			com.example.one_mw.entity.Service service) 
					throws
					InvalidServiceSchemaException, 
					MapExpressionEvaluationException, 
					InvalidMapSchemaException {
				
		new SchemaService().convertToSchemaMap(map, service.getSchemaItemsAsMap("RESPONSE"));
			
	}
	
	public com.example.one_mw.entity.Service verifyServiceCode(String code) throws InvalidServiceException{
		
		this.serviceContainer =null;
		com.example.one_mw.entity.Service service;
		
		try {
			
			logger.info(String.format("Try to verify service code [%s]", code));
			
			if(this.serviceContainer != null) {
				
				//Try to find service in the container
				service=(com.example.one_mw.entity.Service) serviceContainer.get(code);	
				
				if(service==null) {
					
					//Not in container
					//Try to find service from repo
					service = businessServices.getServiceByCode(code);
				}
				
				if(service == null) {
					
					//If null then service not found, raise an error
					throw new InvalidServiceException("Service Code Not Found");
				}
				
				//Add service to container
				this.addServiceToServiceManager(service);
				
				return service;
				
			}
			else {
				
				logger.info(String.format("Get Service code [%s] from repo ..", code));
				//Container is null try to find from repo and add to container, otherwise raise error
				service = businessServices.getServiceByCode(code);
				
				if(service == null) {
					
					logger.error(String.format("Service code [%s] is not found ..", code));
					//If null then service not found, raise an error
					throw new InvalidServiceException("Service Code Not Found");
				}
				else {
					
					//Add service to container
					this.addServiceToServiceManager(service);
					
					logger.info(String.format("Service code [%s] is found ..", code));
					return service;
				}
			}
				
		}
		catch(Exception e) {
			
			logger.error(String.format("Service code [%s]  verification raised Unknown exception..", code));
			logger.error(String.format(e.getMessage()));
			
			throw new InvalidServiceException(String.format("InvalidServiceException Function verifyServiceCode, Service %s", code));
		}
	}

	public com.example.one_mw.entity.Service getServiceFromServiceManager(String code){
		
		return (com.example.one_mw.entity.Service) serviceContainer.get(code);
	}
	
	public Map<String,Object> buildServiceInputSchema(Map<String,Object> map,Map<String,ISchemaItem> schemaItems) throws 
	MapExpressionEvaluationException, 
	InvalidMapSchemaException{
		
		logger.info(String.format("Start converting input to service schema .."));
		
		Map<String,Object> result;
		result = new SchemaService().convertToSchemaMap(map, schemaItems);
		
		result.forEach((k,v)->{
			
			if(!k.contains("password") && v!=null)
				logger.trace(String.format("Service schema result Key[%s], Value[%s]", k,v.toString()));
			else
				logger.trace(String.format("Service schema result Key[%s], Value[null]", k));
		});
		return result;
	}
	
	public Map<String,Object> buildServiceOutputSchema(Map<String,Object> map,Map<String,ISchemaItem> schemaItems) throws 
	MapExpressionEvaluationException, 
	InvalidMapSchemaException{
		
		logger.info(String.format("Start converting output to service schema .."));
		
		Map<String,Object> result = map;
		result = new SchemaService().convertToSchemaMap(map, schemaItems);
		
		result.forEach((k,v)->{
			
			if(!k.contains("password") && v!=null)
				logger.trace(String.format("Service schema result Key[%s], Value[%s]", k,v.toString()));
			else
				logger.trace(String.format("Service schema result Key[%s], Value[null]", k));
		});
		
		return result;
	}

	public String getServiceProcess(com.example.one_mw.entity.Service service,Map<String,Object> requestMap) throws  
	MapExpressionEvaluationException, 
	FailedToFindProcessNameException, 
	InvalidMapSchemaException {
		
		if(service.getIsMakerCheckerEnabled() && requestMap.get("redirect_flag")==null) {
			
			logger.info(String.format("Service [%s] is Maker/Checker Enabled", service.getName()));
			logger.info(String.format("Add redirect_flag to Service [%s] requestMap", service.getName()));
			
			requestMap.put("redirect_flag", false);
		}
		
		//Evaluate Decision Table and return Process Name
		
		logger.info(String.format("Build Service [%s] working Map for REQUEST params", service.getName()));
		
		Map<String,Object> workingMap = buildServiceInputSchema(requestMap, service.getSchemaItemsAsMap("REQUEST"));
		
		workingMap.forEach((k,v)->{
			
			if(!k.contains("password") && v!=null)
				logger.trace(String.format("workingMap Key[%s], Value[%s]", k,v.toString()));
			else
				logger.trace(String.format("workingMap Key[%s], Value[null]", k));
		});
		
		service.setProcessSchemaItems(workingMap);
		
		workingMap.put("CheckOnlyEnabled", service.getIsCheckOnlyRunEnabled());
		workingMap.put("MakerCheckerEnabled", service.getIsMakerCheckerEnabled());
		
		logger.info(String.format("Get Service [%s] Decision Table", service.getName()));
		List<ServiceDecisionLine> decisionTable= service.getServiceDecisionTable();//Will get sorted list
		
		for(ServiceDecisionLine line : decisionTable) {
			
			logger.info(String.format("Evaluate Service [%s] Decision Table Line [%s]", service.getName(),line.getLogicalExpression()));
			if(LanguageExpressionService.evaluateLogicalExpression(workingMap, line.getLogicalExpression())) {
				
				logger.info(String.format("Service [%s] associated process [%s]", service.getName(),line.getProcessName()));
				return line.getProcessName();
			}
		}
		
		logger.error(String.format("Service [%s] Decision Table returned no Process", service.getName()));
		throw new FailedToFindProcessNameException(String.format("FailedToFindProcessNameException, Function getServiceProcess, Service Name %s", service.getName()));
	}
}
