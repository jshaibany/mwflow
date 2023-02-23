package com.example.one_mw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="task_run_def")
public class TaskRunItem {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="TASK_ID")
	private Integer taskId;
	@Column(name="TASK_NAME")
	private String taskName;
	@Column(name="PROCESS_ID")
	private Integer processId;
	@Column(name="PROCESS_NAME")
	private String processName;
	@Column(name="RUN_ORDER")
	private Integer runOrder;
	@Column(name="INPUT_MAP_EXPRESSION")
	private String inputMapExpression;
	@Column(name="START_CONDITION")
	private String startCondition;
	@Column(name="SUCCESS_CONDITION")
	private String successCondition;
	@Column(name="TYPE_ID")
	private String type;
	@Column(name="TYPE_XML_FILE")
	private String typeXMLFile;
	@Column(name="INCLUDE_IN_RESPONSE")
	private Boolean isIncludedInResponse;
	@Column(name="TERMINATE_IF")
	private String terminateCondition;
	
	@OneToOne
	@JoinColumn(name="TASK_ID",insertable=false, updatable=false)
	private Task task;
	
	@ManyToOne
	@JoinColumn(name="PROCESS_ID",insertable=false, updatable=false)
	private Process process;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Integer getProcessId() {
		return processId;
	}
	public void setProcessId(Integer processId) {
		this.processId = processId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public Integer getRunOrder() {
		return runOrder;
	}
	public void setRunOrder(Integer runOrder) {
		this.runOrder = runOrder;
	}
	public String getInputMapExpression() {
		return inputMapExpression;
	}
	public void setInputMapExpression(String inputMapExpression) {
		this.inputMapExpression = inputMapExpression;
	}
	public String getStartCondition() {
		return startCondition;
	}
	public void setStartCondition(String startCondition) {
		this.startCondition = startCondition;
	}
	public String getSuccessCondition() {
		return successCondition;
	}
	public void setSuccessCondition(String successCondition) {
		this.successCondition = successCondition;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeXMLFile() {
		return typeXMLFile;
	}
	public void setTypeXMLFile(String typeXMLFile) {
		this.typeXMLFile = typeXMLFile;
	}
	/**
	 * @return the isIncludedInResponse
	 */
	public Boolean getIsIncludedInResponse() {
		return isIncludedInResponse;
	}
	/**
	 * @param isIncludedInResponse the isIncludedInResponse to set
	 */
	public void setIsIncludedInResponse(Boolean isIncludedInResponse) {
		this.isIncludedInResponse = isIncludedInResponse;
	}
	/**
	 * @return the terminateCondition
	 */
	public String getTerminateCondition() {
		return terminateCondition;
	}
	/**
	 * @param terminateCondition the terminateCondition to set
	 */
	public void setTerminateCondition(String terminateCondition) {
		this.terminateCondition = terminateCondition;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	
}
