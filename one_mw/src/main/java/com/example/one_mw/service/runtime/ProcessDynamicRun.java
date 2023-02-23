package com.example.one_mw.service.runtime;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.one_mw.collections.helper.SortTaskRunPlanItems;
import com.example.one_mw.entity.Process;
import com.example.one_mw.entity.ProcessRun;
import com.example.one_mw.entity.TaskRun;
import com.example.one_mw.entity.TaskRunItem;
import com.example.one_mw.exception.FailedProcessRunException;
import com.example.one_mw.exception.InvalidTaskRunPlan;
import com.example.one_mw.exception.MapExpressionEvaluationException;
import com.example.one_mw.exception.ProcessExpressionEvaluationException;
import com.example.one_mw.exception.TerminatedProcessRunException;
import com.example.one_mw.helper.ProcessRootObjectContainer;
import com.example.one_mw.service.LanguageExpressionService;
import com.example.one_mw.service.RunRepoServices;

public class ProcessDynamicRun implements IProcessDynamicRunnable{

	Logger logger = LogManager.getLogger(ProcessDynamicRun.class);
	
	private ApplicationContext context;
	private ITaskDynamicRunnable taskRun;
	private RunRepoServices repo;	
	private Process process;
	private ProcessRun processRun;
	private List<TaskRunItem> execPlan;
	private ProcessRootObjectContainer container;
	private List<String> processBeans;
	
	public void mergeOutParams(Map<String, Object> outParams) {
		this.container.processOutMap.putAll(outParams);
	}
	
	@Override
	public void initializeComponents(Process process,ProcessRun run,RunRepoServices repo) {
			
		this.repo=repo;
		this.process=process;
		this.processRun = run;
		this.execPlan = process.getTasksRunPlan();	
		this.container = new ProcessRootObjectContainer();
		this.processBeans = new ArrayList<>();
		this.processBeans.add("JSON");
		this.processBeans.add("merger");
			
	}
	
	@Override
	public void validateTaskRunPlan() throws InvalidTaskRunPlan {
		// TODO Auto-generated method stub
		//Check if TaskRunItem(0) is existed
		//Check TaskRunItem(0) XML File & Type ID are not empty or null
		
	}
	
	@Override
	public Map<String, Object> runProcess() throws FailedProcessRunException, TerminatedProcessRunException {
		
		//List of Tasks in run plan
		List<TaskRunItem> runList = new ArrayList<>();
				
		this.container.processInMap = this.process.getInMap();
		
		this.container.processInMap.forEach((k,v)->{
			
			if(!k.contains("password") && v!=null)
			logger.trace(String.format("processInMap Key [%s] Value [%s]", k,v.toString()));
			else
				logger.trace(String.format("processInMap Key [%s] Value [null]", k));
		});
		
		runList = this.execPlan;
		
		logger.info(String.format("Sort the task run plan .."));
		
		//Sort Tasks by run-order
		Collections.sort(runList, new SortTaskRunPlanItems());
		
		runList.forEach(t->{
			
			logger.trace(String.format("Task [%s] Order [%d]", t.getTaskName(),t.getRunOrder()));
		});
		
		for(TaskRunItem item : runList) {
			
			logger.info(String.format("Starting new task run .."));
			
			TaskRun run = new TaskRun();
			
			run.setTaskId(item.getTask().getId());
			run.setTaskName(item.getTask().getName());
			run.setProcessId(item.getProcessId());
			run.setProcessName(item.getProcessName());
			run.setProcessRunId(this.processRun.getId());
			run.setStarTime(Timestamp.valueOf(LocalDateTime.now()));
			run.setStatus("P");
			
			this.repo.createNewTaskRun(run);
			
			logger.info(String.format("Task run [%d] is created ..",run.getId()));
			
			//foreach Task Run:
			
			if(!this.container.terminateProcess) {
				
				//For each task in the Task plan  create a task run instance
				this.context = new ClassPathXmlApplicationContext(item.getTypeXMLFile());
				this.taskRun = (ITaskDynamicRunnable) context.getBean(item.getType());
				
				logger.debug(String.format("Context file [%s] ..",item.getTypeXMLFile()));
				logger.debug(String.format("Task run file [%s] ..",item.getType()));
				
				Boolean startFalg;
				
				try {
					
					logger.info(String.format("Evaluate task run [%d] Start_If expression ..",run.getId()));
					
					startFalg=startThisTask(item,container);
					
					logger.info(String.format("Start_If expression evaluated to [%s]: ",startFalg.toString()));
					
				}
				catch(Exception e) {
					
					logger.error(String.format("Evaluate task run [%d] Start_If expression error ..",run.getId()));
					logger.error(e.getMessage());
					
					e.printStackTrace();
					
					throw new FailedProcessRunException(String.format("FailedProcessRunException startThisTask Expression Evaluation Process Name:%s , Task Name:%s", this.process.getName(),item.getTaskName()));
				}
				
				if(startFalg) {
					
					logger.info(String.format("Update current Task [%s] in process ",item.getTaskName()));
					
					this.container.currentTask=item.getTaskName();
					
					logger.info(String.format("Start building task run [%d] context ..",run.getId()));
					
					this.taskRun.buildContext(item.getTask());
					
					Map<String, Object> p;
					
					try {
						
						try {
							
							logger.info(String.format("Start getting task input params .."));
							
							p = this.getTaskInputParams(item,container);
							
							if(p!=null) {
								
								/*
								 * In case Task Run has an expression for input map
								 */
								logger.info(String.format("Start getting task input params from task schema .."));
								
								p.forEach((k,v)->{
									
									if(!k.contains("password") && v!=null)
									logger.trace(String.format("taskInMap [%s] Key [%s] Value [%s]",item.getTaskName(), k,v.toString()));
									else
										logger.trace(String.format("taskInMap [%s] Key [%s] Value [null]",item.getTaskName(), k));
								});
								
								logger.info(String.format("Start building task run [%d] params ..",run.getId()));
								
								this.taskRun.buildRequestParameters(p);
								
								logger.info(String.format("Update process container taskInMap, Task Name [%s] ..",item.getTaskName()));
								
								this.container.taskInMap.put(item.getTaskName(), p);
							}		
							else {
								
								/*
								 * In case Task Run has no expression for input map, get the process input map
								 */
								
								logger.info(String.format("Start getting task input params from process inMap .."));
								
								logger.info(String.format("Start building task run [%d] params ..",run.getId()));
								
								this.taskRun.buildRequestParameters(process.getInMap());
								
								logger.info(String.format("Update process container taskInMap, Task Name [%s] ..",item.getTaskName()));
								
								this.container.taskInMap.put(item.getTaskName(), process.getInMap());
							}
						}
						catch(Exception e) {
							
							logger.error(String.format("Task run [%d] error while processing params..",run.getId()));
							logger.error(e.getMessage());
							
							e.printStackTrace();
							
							throw new FailedProcessRunException(String.format("FailedProcessRunException Fail to parse Task params Process Name:%s , Task Name:%s", this.process.getName(),item.getTaskName()));
						}
						
						
						try {
							
							logger.info(String.format("Starting task run [%d] params validation..",run.getId()));
							
							this.taskRun.validateTasKParameters();
							
							logger.info(String.format("Run Task [%d] and save results to process container..",run.getId()));
							//Store task result to be merged in single out Map
							this.container.taskOutMap.put(item.getTaskName(), taskRun.runTask());
							
							//Merge the task result in single out Map
							if(item.getIsIncludedInResponse()) {
								
								logger.info(String.format("Starting merging task run [%d] params in process outMap..",run.getId()));
								
								this.mergeOutParams(this.container.taskOutMap.get(item.getTaskName()));
							}
							
							
						}
						catch(Exception e) {
							
							logger.error(String.format("Error during task [%d] running..",run.getId()));
							logger.error(String.format("Task [%s] is failed..",item.getTaskName()));
							logger.error(e.getMessage());
							
							e.printStackTrace();
							
							throw new FailedProcessRunException(String.format("FailedProcessRunException Fail in Task processing Process Name:%s , Task Name:%s", this.process.getName(),item.getTaskName()));
						}
						
					    try {
					    	
					    	logger.info(String.format("Start evaluate if task run [%d] is successfull..",run.getId()));
					    	
					    	if(taskIsSuccess(item, container)) {
								
					    		logger.info(String.format("Update Task Run [%d] as completed [C]",run.getId()));
					    		
								run.setStatus("C");
								run.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
								
								this.container.taskRunStatus.put(run.getTaskName(), true);
							}
							else {
								
								logger.info(String.format("Update Task Run [%d] as failed [F]",run.getId()));
								
								run.setStatus("F");
								run.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
								
								this.container.taskRunStatus.put(run.getTaskName(), false);
							}
						    
							
							this.repo.updateTaskRun(run);
							
							this.container.completedTasks.add(item.getTaskName());
					    }
					    catch(Exception e) {
					    	
					    	logger.error(String.format("Error during condition expression evaluation, Task Run [%d] ",run.getId()));
					    	logger.error(e.getMessage());
					    	
					    	e.printStackTrace();
					    	
					    	logger.error(String.format("Update Task Run [%d] as failed [F]",run.getId()));
					    	
					    	run.setStatus("F");
							run.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
							
							this.repo.updateTaskRun(run);
							
					    	throw new FailedProcessRunException(String.format("FailedProcessRunException Fail in taskIsSuccess Expression Evaluation Process Name:%s , Task Name:%s", this.process.getName(),item.getTaskName()));
					    }
						
						
					    try {
					    	
					    	logger.info(String.format("Evaluate Task Run [%d] Terminate_If condition expression",run.getId()));
					    	
					    	this.container.terminateProcess = terminateTheProcess(item, container);
					    }
					    catch(Exception e) {
					    	
					    	logger.error(String.format("Error in Task Run [%d] Terminate_If condition expression evaluation",run.getId()));
					    	logger.error(e.getMessage());
					    	
					    	e.printStackTrace();
					    	
					    	logger.error(String.format("Update Task Run [%d] as failed [F]",run.getId()));
					    	
					    	run.setStatus("F");
							run.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
							
							this.repo.updateTaskRun(run);
					    	
					    	throw new FailedProcessRunException(String.format("FailedProcessRunException Fail in terminateTheProcess Expression Evaluation Process Name:%s , Task Name:%s", this.process.getName(),item.getTaskName()));
					    }
						
						
					} catch (Exception e) {
						
						logger.error(String.format("Task Run [%d] unknown exception",run.getId()));
						logger.error(e.getMessage());
						
						e.printStackTrace();
						
						logger.error(String.format("Update Task Run [%d] as failed [F]",run.getId()));
						logger.error(e.getMessage());
						
						run.setStatus("F");
						run.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
						
						this.repo.updateTaskRun(run);
						
						throw new FailedProcessRunException(String.format("FailedProcessRunException Process Name:%s", this.process.getName()));
					}
		    
				}
			}
			else {
				
				logger.error(String.format("Task Run [%d] marked as Terminated ..",run.getId()));
				logger.error(String.format("Update Task Run [%d] as Terminated [T]",run.getId()));
				
				run.setStatus("T");
				run.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
				
				this.repo.updateTaskRun(run);
				
				throw new TerminatedProcessRunException(String.format("TerminatedProcessRunException Terminated by Terminate_If Expression Process Name:%s Task Name:%s", this.process.getName(),item.getTaskName()));
			}	
		}
		
		logger.info(String.format("Start process output preparation, Process [%s], Process Run [%d]", this.process.getName(),this.processRun.getId()));
		
		//check the whole process results to return the proper reply
		return prepareProcessOutput(this.container);
	}

	/*
	 * @return Map<String, Object>
	 * getTaskInputParams is used to evaluate expression in the task run item which define the input elements
	 * in case no expression found return null
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getTaskInputParams(TaskRunItem item, ProcessRootObjectContainer container)
			throws ProcessExpressionEvaluationException {

		if(item.getInputMapExpression()!=null && !item.getInputMapExpression().isEmpty()) {
			
			try {
				
				logger.info(String.format("Evaluate Task Run [%d] Item expression [%s]",item.getId(), item.getInputMapExpression()));
				return (Map<String, Object>) LanguageExpressionService.evaluateMapExpressionToObject(container, item.getInputMapExpression(),this.processBeans);
				
			} catch (MapExpressionEvaluationException e) {
				
				logger.error(String.format("Erro when evaluate Task Run [%d] Item expression [%s]",item.getId(), item.getInputMapExpression()));
				e.printStackTrace();
				throw new ProcessExpressionEvaluationException("ProcessExpressionEvaluationException/MapExpressionEvaluationException - getTaskInputParams - "+item.getTaskName());
			}
			catch (Exception e) {
				
				logger.error(String.format("Unknown exception when evaluate Task Run [%d] Item expression [%s]",item.getId(),item.getInputMapExpression()));
				logger.error(e.getMessage());
				
				e.printStackTrace();
				throw new ProcessExpressionEvaluationException("ProcessExpressionEvaluationException/Exception - getTaskInputParams - "+item.getTaskName());
			}
		}
		//Else return null
		
		return null;
		
	}
	
	private Boolean startThisTask(TaskRunItem item,ProcessRootObjectContainer container) throws ProcessExpressionEvaluationException, MapExpressionEvaluationException {
		
		//Evaluate the task run start condition (Expression)
		if(item.getStartCondition()!=null && !item.getStartCondition().isEmpty()) {
			
			logger.info(String.format("Evaluate Task Run [%d] Start_If condition expression [%s]",item.getId(), item.getStartCondition()));
			Object result= LanguageExpressionService.evaluateMapExpressionToObject(container, item.getStartCondition(),this.processBeans);
			
			if(result instanceof Boolean) {
				
				logger.info(String.format("Evaluate Task Run [%d] Start_If condition expression [%s] is [%s]",item.getId(), item.getStartCondition(),result));
				return (Boolean) result;
			}
			else {
				
				logger.error(String.format("Evaluate Task Run [%d] Start_If condition expression [%s] is Unknown [Not Boolean]",item.getId(),item.getStartCondition()));
				throw new ProcessExpressionEvaluationException("ProcessExpressionEvaluationException - startThisTask - "+item.getTaskName());
			}
		}
		//Else if no expression found the will consider it true by default
		
		logger.info(String.format("Evaluate Task Run [%d] Start_If, no expression is found, function will return default:true",item.getId()));
		return true;
	}
	
	private Boolean terminateTheProcess(TaskRunItem item,ProcessRootObjectContainer container) throws ProcessExpressionEvaluationException,MapExpressionEvaluationException {
		
		//Evaluate the task run start condition (Expression)
		if(item.getTerminateCondition()!=null && !item.getTerminateCondition().isEmpty()) {
			
			logger.info(String.format("Evaluate Task Run [%d] Terminate_If condition expression [%s]",item.getId(), item.getTerminateCondition()));
			Object result= LanguageExpressionService.evaluateMapExpressionToObject(container, item.getTerminateCondition(),this.processBeans);
					
			if(result instanceof Boolean) {
						
				logger.info(String.format("Evaluate Task Run [%d] Terminate_If condition expression [%s] is [%s]",item.getId(), item.getTerminateCondition(),result));
				return (Boolean) result;
					
			}
			else {
						
				logger.error(String.format("Evaluate Task Run [%d] Terminate_If condition expression [%s] is Unknown [Not Boolean]",item.getId(),item.getTerminateCondition()));
				throw new ProcessExpressionEvaluationException("ProcessExpressionEvaluationException - startThisTask - "+item.getTaskName());
					
			}
				
		}
		//Else if no expression found the will consider it false by default
				
		logger.info(String.format("Evaluate Task Run [%d] Terminate_If, no expression is found, function will return default:false",item.getId()));		
		return false;
	}
	
	private Boolean taskIsSuccess(TaskRunItem item,ProcessRootObjectContainer container) throws ProcessExpressionEvaluationException,MapExpressionEvaluationException {
		
		//Evaluate the task run start condition (Expression)
		if(item.getSuccessCondition()!=null && !item.getSuccessCondition().isEmpty()) {
					
			logger.info(String.format("Evaluate Task Run [%d] SUCCESS_CONDITION  expression [%s]",item.getId(), item.getSuccessCondition()));
			Object result= LanguageExpressionService.evaluateMapExpressionToObject(container, item.getSuccessCondition(),this.processBeans);
					
			if(result instanceof Boolean) {
					
				logger.info(String.format("Evaluate Task Run [%d] SUCCESS_CONDITION  expression [%s] is [%s]",item.getId(), item.getSuccessCondition(),result));
				return (Boolean) result;
					
			}
			else {
						
				logger.error(String.format("Evaluate Task Run [%d] SUCCESS_CONDITION expression [%s] is Unknown [Not Boolean]",item.getId(),item.getSuccessCondition()));
				throw new ProcessExpressionEvaluationException("ProcessExpressionEvaluationException - startThisTask - "+item.getTaskName());
					
			}
				
		}
		//Else if no expression found the will consider it true by default
				
		logger.info(String.format("Evaluate Task Run [%d] SUCCESS_CONDITION, no expression is found, function will return default:true",item.getId()));			
		return true;
	}

	private Map<String,Object> prepareProcessOutput(ProcessRootObjectContainer container){
		
		Map<String,Object> result = new HashMap<>();
		Map<String,Boolean> processTasksResult = container.taskRunStatus;
	
		Integer counter=0;
		
		for(Map.Entry<String, Boolean> entry: processTasksResult.entrySet()) {
			
			if(!entry.getValue()) {
				
				counter++;
			}
		}
		
		if(counter <=0 ) {
			
			result.put("processOutput", container.processOutMap);
			result.put("result", 0);
			result.put("message", "Success");
			
			return result;
		}
		if(counter >= processTasksResult.size()) {
			
			//All Tasks failed
			result.put("result", -1);
			result.put("message", "Failed");
			result.put("processOutput", null);
			
			return result;
			
		}
		else {
			
			//Some Tasks failed
			result.put("processOutput", container.processOutMap);
			result.put("result", 1);
			result.put("message", String.format("Process completed with %d failed tasks", counter));
			
			return result;
		}
		
	}
}
