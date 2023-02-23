package com.example.one_mw.service.runtime;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.one_mw.entity.Task;
import com.example.one_mw.exception.FailedTaskRunException;
import com.example.one_mw.exception.HttpResponseToMapConvertException;
import com.example.one_mw.exception.InvalidHttpRequestMethod;
import com.example.one_mw.exception.InvalidMapSchemaException;
import com.example.one_mw.exception.InvalidTaskSchemaException;
import com.example.one_mw.exception.MapExpressionEvaluationException;
import com.example.one_mw.service.SchemaService;

public class TaskDynamicRun implements ITaskDynamicRunnable {


	Logger logger = LogManager.getLogger(TaskDynamicRun.class);
	
	private static final SchemaService schemaService= new SchemaService();
	
	private Task task;
	private ApplicationContext context;
	private IntegratedServicesObjRunTime service;
	private Map<String, Object> inParams;
	private Map<String, Object> requestParams;
	private Map<String, Object> responseParams;
	
	@Override
	public void buildContext(Task task) {
		
		this.inParams=new HashMap<>();
		this.requestParams=new HashMap<>();
		
		this.task=task;
		
		this.context = new ClassPathXmlApplicationContext(task.getTypeXMLFile());
		this.service = (IntegratedServicesObjRunTime) context.getBean(task.getType());
		
		logger.info(String.format("Task [%s] context file [%s]", task.getName(),task.getTypeXMLFile()));
		logger.info(String.format("Integrated Service bean file [%s]", task.getType()));
	
	}
	
	@Override
	public void buildRequestParameters(Map<String, Object> map) throws MapExpressionEvaluationException, InvalidTaskSchemaException, InvalidMapSchemaException {
		

		map.forEach((k,v)->{
			
			this.inParams.put(k, v);
		});
		/*
		if(this.inParams==null)
			this.inParams=new HashMap<>();
		
		if(inParams!=null)
			this.inParams=inParams;
		
		if(this.requestParams==null)
			this.requestParams=new HashMap<>();*/
		
		logger.info(String.format("Start to build task [%s] schema params, Direction=REQUEST ..", this.task.getName()));
		requestParams = schemaService.convertToSchemaMap(this.inParams, this.task.getSchemaItemsAsMap("REQUEST"));
		
	}
	
	@Override
	public void validateTasKParameters() {
		// TODO Auto-generated method stub
		
		//If Not valid
		//throw new InvalidTaskParametersException("");
		
	}

	@Override
	public Map<String, Object> runTask() throws FailedTaskRunException {
		
		logger.info(String.format("Start building integrated service [%s] context", task.getTaskService().getName()));
		service.buildContext(task.getTaskService());
		
		Map<String, Object> response;
		
		try {
			
			logger.info(String.format("Begin request ..."));
			response = service.doRequest(requestParams, task);
			
			logger.info(String.format("Getting response ..."));
			if(response!=null) {
				
				logger.info(String.format("Start building response params..."));
				return buildResponseParameters(response);
			}
			
			logger.info(String.format("Task service request's response is null ..."));
			logger.info(String.format("Task returning null ..."));
			return null;
				
		}
		catch(InvalidMapSchemaException e) {
			
			logger.error(String.format("Exception [%s] in service request ..", e.getClass().getName()));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			
			throw new FailedTaskRunException(String.format("FailedTaskRunException / InvalidMapSchemaException", this.task.getName()));
		}
		catch(InvalidTaskSchemaException e) {
			
			logger.error(String.format("Exception [%s] in service request ..", e.getClass().getName()));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			
			throw new FailedTaskRunException(String.format("FailedTaskRunException / InvalidTaskSchemaException", this.task.getName()));
		}
		catch(HttpResponseToMapConvertException e) {
			
			logger.error(String.format("Exception [%s] in service request ..", e.getClass().getName()));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			
			throw new FailedTaskRunException(String.format("FailedTaskRunException / HttpResponseToMapConvertException", this.task.getName()));
		}
		catch(InvalidHttpRequestMethod e) {
			
			logger.error(String.format("Exception [%s] in service request ..", e.getClass().getName()));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			
			throw new FailedTaskRunException(String.format("FailedTaskRunException / InvalidHttpRequestMethod", this.task.getName()));
		}
		catch(MapExpressionEvaluationException e) {
			
			logger.error(String.format("Exception [%s] in service request ..", e.getClass().getName()));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			
			throw new FailedTaskRunException(String.format("FailedTaskRunException / MapExpressionEvaluationException", this.task.getName()));
		}
		catch(Exception e) {
			
			logger.error(String.format("Exception [%s] in service request ..", e.getClass().getName()));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			
			throw new FailedTaskRunException(String.format("FailedTaskRunException Unknown", this.task.getName()));
		}
		
		
		
		
	}
	
	private Map<String, Object> buildResponseParameters(Map<String, Object> params) throws MapExpressionEvaluationException, InvalidTaskSchemaException, InvalidMapSchemaException{
		
		if(this.responseParams==null)
			this.responseParams=new HashMap<>();
		
		logger.info(String.format("Start to build task [%s] schema params, Direction=RESPONSE ..", this.task.getName()));
		responseParams = schemaService.convertToSchemaMap(params,this.task.getSchemaItemsAsMap("RESPONSE"));
		
		return responseParams;
	}
}
