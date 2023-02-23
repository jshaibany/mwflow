package com.example.one_mw.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="task_run")
public class TaskRun {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="TASK_ID")
	private Integer taskId;
	@Column(name="TASK_NAME")
	private String taskName;
	@Column(name="PROCESS_ID")
	private Integer processId;
	@Column(name="PROCESS_NAME")
	private String processName;
	@Column(name="PROCESS_RUN_ID")
	private BigInteger processRunId;
	@Column(name="START_TIME")
	private Timestamp starTime;
	@Column(name="END_TIME")
	private Timestamp endTime;
	@Column(name="STATUS")
	private String status;
	@Column(name="REMARKS")
	private String remarks;
	
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
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
	public BigInteger getProcessRunId() {
		return processRunId;
	}
	public void setProcessRunId(BigInteger processRunId) {
		this.processRunId = processRunId;
	}
	public Timestamp getStarTime() {
		return starTime;
	}
	public void setStarTime(Timestamp starTime) {
		this.starTime = starTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
