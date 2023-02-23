package com.example.one_mw.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessRootObjectContainer {

	//@Variable processInMap ,To hold Process Run inMap Parameters 
	public Map<String,Object> processInMap; 
	//@Variable processOutMap ,To hold Process Run outMap Parameters, it a collection of all task outMaps (Included=true) only
	public Map<String,Object> processOutMap; 
	//@Variable taskOutMap ,To hold Task Names (As in Task Run Plan) along with the outMap of the task run
	public Map<String,Map<String,Object>> taskOutMap; 
	//@Variable taskInMap ,To hold Task Names (As in Task Run Plan) along with the inMap of the task run 
	public Map<String,Map<String,Object>> taskInMap; 
	//@Variable taskRunStatus ,To hold each Task post run result (true/false)
	public Map<String,Boolean> taskRunStatus; 
	//@Variable terminateProcess ,To hold each Task TERMINATE_IF expression evaluation result (true/false)
	public Boolean terminateProcess; 
	//@Variable completedTasks ,List of Task Names in the process which is completed
	public List<String> completedTasks;
	//@Variable currentTask ,Name of the currently running task
	public String currentTask;
	
	public ProcessRootObjectContainer() {
		super();
		this.processInMap = new HashMap<>();
		this.processOutMap = new HashMap<>();
		this.taskOutMap = new HashMap<>();
		this.taskInMap = new HashMap<>();
		this.taskRunStatus = new HashMap<>();
		this.terminateProcess = false;
		this.completedTasks = new ArrayList<>();
		this.currentTask = "";
	}
}
