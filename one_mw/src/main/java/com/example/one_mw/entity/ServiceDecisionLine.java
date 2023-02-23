package com.example.one_mw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="service_decision_table")
public class ServiceDecisionLine {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="SERVICE_ID")
	private Integer serviceId;
	@Column(name="PROCESS_NAME")
	private String processName;
	@Column(name="LOGICAL_EXPRESSION")
	private String logicalExpression;
	@Column(name="PRIORITY_RUN")
	private Integer priority;
	
	@ManyToOne
	@JoinColumn(name="SERVICE_ID",insertable=false, updatable=false)
	private Service service;
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the serviceId
	 */
	public Integer getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}
	/**
	 * @param processName the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	/**
	 * @return the logicalExpression
	 */
	public String getLogicalExpression() {
		return logicalExpression;
	}
	/**
	 * @param logicalExpression the logicalExpression to set
	 */
	public void setLogicalExpression(String logicalExpression) {
		this.logicalExpression = logicalExpression;
	}
	/**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}
