package com.example.one_mw.service.runtime;

import java.util.Map;

import com.example.one_mw.entity.Task;
import com.example.one_mw.exception.FailedTaskRunException;
import com.example.one_mw.exception.InvalidMapSchemaException;
import com.example.one_mw.exception.InvalidTaskParametersException;
import com.example.one_mw.exception.InvalidTaskSchemaException;
import com.example.one_mw.exception.MapExpressionEvaluationException;

public interface ITaskDynamicRunnable {

	/*
	 * buildContext function is the first one to call, to build the integrated service instance
	 */
	public void buildContext(Task task);
	/*
	 * validateTasKParameters is to validate the task params and if issue is found an exception should be fired
	 * in case no issues found the function should return nothing
	 */
	public void validateTasKParameters() throws InvalidTaskParametersException;
	/*
	 * @return Map<String, Object>
	 * runTask is to run the task by getting the designated integrated service instance and execute
	 */
	public Map<String,Object> runTask() throws FailedTaskRunException;
	/*
	 * buildParameters should be called after getTaskInputParams, it should build the input params for the integrated service
	 */
	public void buildRequestParameters(Map<String,Object> inParams) throws MapExpressionEvaluationException, InvalidTaskSchemaException, InvalidMapSchemaException;
}
