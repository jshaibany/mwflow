package com.example.one_mw.service.runtime;

import java.util.Map;

import com.example.one_mw.entity.Process;
import com.example.one_mw.entity.ProcessRun;
import com.example.one_mw.exception.FailedProcessRunException;
import com.example.one_mw.exception.InvalidTaskRunPlan;
import com.example.one_mw.exception.TerminatedProcessRunException;
import com.example.one_mw.service.RunRepoServices;

public interface IProcessDynamicRunnable {

	public void validateTaskRunPlan() throws InvalidTaskRunPlan;
	public Map<String,Object> runProcess() throws FailedProcessRunException, TerminatedProcessRunException;
	/*
	 * @return void
	 * initializeComponents 
	 */
	public void initializeComponents(Process process, ProcessRun run, RunRepoServices repo);
	
}
