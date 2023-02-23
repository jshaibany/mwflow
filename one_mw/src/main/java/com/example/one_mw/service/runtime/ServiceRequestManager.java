package com.example.one_mw.service.runtime;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.example.one_mw.entity.Process;
import com.example.one_mw.entity.ProcessRun;
import com.example.one_mw.entity.ServiceBulkConfig;
import com.example.one_mw.entity.ServiceBulkRequest;
import com.example.one_mw.entity.ServiceBulkRequestLine;
import com.example.one_mw.entity.ServiceRequest;
import com.example.one_mw.entity.ServiceRequestLine;
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
import com.example.one_mw.service.ProcessService;
import com.example.one_mw.service.RunRepoServices;
import com.example.one_mw.service.ServiceRequestService;

@Service
public class ServiceRequestManager {

	Logger logger = LogManager.getLogger(ServiceRequestManager.class);
	
    private final BusinessServiceManager serviceManager;
	
	private final ServiceRequestService serviceRequestServices;
	
	private final RequestApprovalManager requestApprovalManager;
	
	private final ProcessService processService;
	
	private final RunRepoServices runRepoServices;
	
	@Autowired
	public ServiceRequestManager(BusinessServiceManager serviceManager, ServiceRequestService serviceRequestServices,
			RequestApprovalManager requestApprovalManager, ProcessService processService,
			RunRepoServices runRepoServices) {
		super();
		this.serviceManager = serviceManager;
		this.serviceRequestServices = serviceRequestServices;
		this.requestApprovalManager = requestApprovalManager;
		this.processService = processService;
		this.runRepoServices = runRepoServices;
	}
	
	public com.example.one_mw.entity.Service getService(String code) throws InvalidServiceException{
		
		logger.info(String.format("Starting service code verification [%s]", code));
		
		return serviceManager.verifyServiceCode(code);
	}
	
	public String getServiceBulkProcess(Integer service_id) {
		
		List<ServiceBulkConfig> serviceBulkConfig = serviceRequestServices.getServiceBulkConfig(service_id);
		
		if(serviceBulkConfig != null && serviceBulkConfig.size()==1) {
			
			return serviceBulkConfig.get(0).getReferenceProcessName();
		}
		
		return null;
	}
	
	public com.example.one_mw.entity.Service initializeServiceRequest(Map<String,Object> map,
			com.example.one_mw.entity.Service service) throws   
	MapExpressionEvaluationException, 
	FailedToFindProcessNameException, 
	InvalidMapSchemaException, 
	InvalidServiceSchemaException {
		
		logger.info(String.format("Initializing service request for service [%s]", service.getName()));
		logger.info(String.format("Starting service schema validation for service [%s]", service.getName()));
		
		serviceManager.validateServiceSchema(map, service);
			
		logger.info(String.format("Start finding associated process .."));
		
		String n=serviceManager.getServiceProcess(service, map);
		
		service.processName=n;
		
		return service;
	}
	
	public Map<String,Object> startProcess(String processName,com.example.one_mw.entity.Service service) 
			throws InvalidTaskSchemaException, 
			MapExpressionEvaluationException, 
			InvalidHttpRequestMethod, 
			HttpResponseToMapConvertException, 
			InvalidTaskParametersException, 
			ProcessExpressionEvaluationException, 
			FailedProcessRunException, 
			TerminatedProcessRunException {
		
		logger.info(String.format("Get process object [%s] ..",processName));
		
		Process process = processService.getProcessByName(processName);
		
		logger.info(String.format("Setting input Map .."));
		
		process.setInMap(service.getProcessSchemaItems());
		
		logger.info(String.format("Get context objects .."));
		
		ApplicationContext context = new ClassPathXmlApplicationContext(process.getTypeXMLFile());
		IProcessDynamicRunnable processRun = (IProcessDynamicRunnable) context.getBean(process.getType());
		
		logger.debug(String.format("Context file [%s] ..",process.getTypeXMLFile()));
		logger.debug(String.format("Process run file [%s] ..",process.getType()));
		
		logger.info(String.format("Starting process run thread .."));
		
		ProcessRun pThread = new ProcessRun();
		pThread.setStarTime(Timestamp.valueOf(LocalDateTime.now()));
		pThread.setProcessId(process.getId());
		pThread.setServiceId(service.getId());
		pThread.setProcessName(process.getName());
		pThread.setStatus("P");
		
		logger.info(String.format("Initializing process run components .."));
		
		processRun.initializeComponents(process, runRepoServices.createNewProcessRun(pThread),runRepoServices);
		
		logger.info(String.format("Process run [%d] is created ..",pThread.getId()));
		
		Map<String,Object> result= new HashMap<>();
		
		try {
			
			logger.info(String.format("Run the process [%d] ..",pThread.getId()));
			
			result=processRun.runProcess();
			
			logger.info(String.format("Process [%d] is completed ..",pThread.getId()));
			logger.info(String.format("Update process [%d] repo as completed [C] ..",pThread.getId()));
			
			pThread.setStatus("C");
			pThread.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
			runRepoServices.updateProcessRun(pThread);
		}
		catch(FailedProcessRunException e) {
			
			logger.error(String.format("Process [%d] raised an error ..",pThread.getId()));
			
			e.printStackTrace();
			
			logger.error(String.format("Update process repo as failed [F] prcocess No:[%d] ..",pThread.getId()));
			
			pThread.setStatus("F");
			pThread.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
			runRepoServices.updateProcessRun(pThread);
			
			logger.error(String.format("Initialize process exception [%s], prcocess No:[%d]..",e.getClass(),pThread.getId()));
			
			throw new FailedProcessRunException(String.format("FailedProcessRunException: Process No:%d", pThread.getId()));
			
		} catch (TerminatedProcessRunException e) {
			
			logger.error(String.format("Process [%d] raised an error ..",pThread.getId()));
			
			e.printStackTrace();
			
			logger.error(String.format("Update process repo as terminated [T] prcocess No:[%d] ..",pThread.getId()));
			
			pThread.setStatus("T");
			pThread.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
			runRepoServices.updateProcessRun(pThread);
			
			logger.error(String.format("Initialize process exception [%s], prcocess No:[%d]..",e.getClass(),pThread.getId()));
			
			throw new TerminatedProcessRunException(String.format("TerminatedProcessRunException: Process No:%d", pThread.getId()));
		} catch (Exception e) {
			
			logger.error(String.format("Process [%d] raised an error ..",pThread.getId()));
			
			e.printStackTrace();
			
			logger.error(String.format("Update process repo as failed [F] prcocess No:[%d] ..",pThread.getId()));
			
			pThread.setStatus("F");
			pThread.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
			runRepoServices.updateProcessRun(pThread);
			
			logger.error(String.format("Initialize process exception [%s], prcocess No:[%d]..",e.getClass(),pThread.getId()));
			
			throw new FailedProcessRunException(String.format("FailedProcessRunException: Process No:%d", pThread.getId()));
		}
		
		return result;
		
	}
	
	public Map<String,Object> prepareServiceOutputMap(com.example.one_mw.entity.Service service,Map<String,Object> map) throws 
	InvalidMapSchemaException, MapExpressionEvaluationException{
		
		return serviceManager.buildServiceOutputSchema(map, service.getSchemaItemsAsMap("RESPONSE"));
	}
	
	public ServiceRequest createNewServiceRequest(com.example.one_mw.entity.Service service,Map<String,Object> lines) throws CreateServiceRequestLineException, CreateApprovalListException {
		
		ServiceRequest request=new ServiceRequest();
		
		request.setServiceId(service.getId());
		request.setServiceName(service.getCode());
		request.setServiceRequestMaker(service.getServiceMaker());
		request.setStatus("P");
		request.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
		
		request.setRequestMsisdn(lines.get("msisdn").toString());
		request.setRequestAccountId(lines.get("account_id").toString());
		if(lines.get("group_id")!=null)
		request.setGroupId(lines.get("group_id").toString());
		if(lines.get("layer_id")!=null)
		request.setLayerId(lines.get("layer_id").toString());
		request.setServiceRequestedBy(lines.get("username").toString());
		
		serviceRequestServices.createNewServiceRequest(request);
		
		if(request.getId()!= null) {
			
			try {
				
				for(Map.Entry<String, Object> entry : lines.entrySet()) {
					
					createServiceRequestLine(request, entry.getKey(), entry.getValue().toString(), "SERVICE_REQUEST_MANAGER");
				}
			}
			catch(Exception e) {
				
				e.printStackTrace();
				
				throw new CreateServiceRequestLineException("");
			}

			try {
				
				String currentApprover=requestApprovalManager.createApprovalList(request.getId(), service.getServiceCheckers());
				
				if(currentApprover!=null) {
					
					request.setNextChecker(currentApprover);
					serviceRequestServices.updateServiceRequest(request);
				}
			}
			catch(Exception e) {
				
				e.printStackTrace();
				
				throw new CreateApprovalListException("");
			}
			
		}
		else {
			
			return null;
		}
			
		
		return request;
	}

	private ServiceRequestLine createServiceRequestLine(ServiceRequest request,String paramName,String paramValue,String paramSource) {
		
		ServiceRequestLine line = new ServiceRequestLine();
		
		line.setRequestId(request.getId());
		line.setParamName(paramName);
		line.setParamValue(paramValue);
		line.setParamSource(paramSource);
		
		return serviceRequestServices.createNewRequestLine(line);
	}
	
	public ServiceBulkRequest createNewServiceRequest(com.example.one_mw.entity.Service service,Map<String,Object> header,List<Map<String,Object>> lines) throws CreateServiceRequestLineException, CreateApprovalListException {
		
		ServiceBulkRequest request=new ServiceBulkRequest();
		
		request.setServiceId(service.getId());
		request.setServiceName(service.getCode());
		request.setServiceRequestMaker(service.getServiceMaker());
		request.setStatus("P");
		request.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
		
		request.setRequestMsisdn(("967775651470").toString());
		request.setRequestAccountId(("1").toString());
		//if(lines.get("group_id")!=null)
		request.setGroupId(("group_id").toString());
		//if(lines.get("layer_id")!=null)
		request.setLayerId(("layer_id").toString());
		request.setServiceRequestedBy(header.get("username").toString());
		
		serviceRequestServices.createNewServiceRequest(request);
		
		if(request.getId()!= null) {
			
			try {
				
				createServiceBulkRequestLines(request, lines);
			}
			catch(Exception e) {
				
				e.printStackTrace();
				
				throw new CreateServiceRequestLineException("");
			}

			try {
				
				String currentApprover=requestApprovalManager.createBulkApprovalList(request.getId(), service.getServiceCheckers());
				
				if(currentApprover!=null) {
					
					request.setNextChecker(currentApprover);
					serviceRequestServices.updateServiceRequest(request);
				}
			}
			catch(Exception e) {
				
				e.printStackTrace();
				
				throw new CreateApprovalListException("");
			}
			
		}
		else {
			
			return null;
		}
			
		
		return request;
	}

	private List<ServiceBulkRequestLine> createServiceBulkRequestLines(ServiceBulkRequest request,List<Map<String,Object>> lines) {
		
		int counter =1;
		List<ServiceBulkRequestLine> result = new ArrayList<>();
		
		for(Map<String,Object> m : lines) {
			
			for(Map.Entry<String, Object> entry : m.entrySet()) {
				
				ServiceBulkRequestLine line = new ServiceBulkRequestLine();
				
				line.setRequestId(request.getId());
				line.setLineNumber(counter);
				line.setParamName(entry.getKey());
				line.setParamValue(entry.getValue().toString());
				line.setParamSource("BULK_REQUEST");
				
				serviceRequestServices.createNewRequestLine(line);
				
				result.add(line);
			}
			
			counter++;
		}
		
		
		return result;
	}
	
	public Boolean authorizedRequestChecker(BigInteger requestId,String accountId,Map<String,Object> privileges) {
		
		ServiceRequest sr = serviceRequestServices.getServiceRequestById(requestId);
		
		if(!sr.getRequestAccountId().contentEquals(accountId))
			return false;
		if(!privileges.containsValue(sr.getNextChecker()))
			return false;
		
		return true;
	}
	
	public Boolean authorizedBulkRequestChecker(BigInteger requestId,String accountId,Map<String,Object> privileges) {
		
		ServiceBulkRequest sr = serviceRequestServices.getServiceBulkRequestById(requestId);
		
		if(!sr.getRequestAccountId().contentEquals(accountId))
			return false;
		if(!privileges.containsValue(sr.getNextChecker()))
			return false;
		
		return true;
	}
}
