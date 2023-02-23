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
@Table(name="service_bulk_processing_schedule")
public class ServiceBulkProcessingSchedule {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="BULK_HEADER_ID")	
	private BigInteger headerId;
	@Column(name="SCHEDULED_BY")	
	private String scheduledBy;
	@Column(name="SCHEDULED_ON")	
	private Timestamp scheduledOn;
	@Column(name="STATUS")	
	private String status;
	@Column(name="ACTUAL_RUNTIME_START")	
	private Timestamp actualRuntimeStart;
	@Column(name="ACTUAL_RUNTIME_END")	
	private Timestamp actualRuntimeEnd;
	@Column(name="CREATED_ON")	
	private Timestamp createdOn;
	
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public BigInteger getHeaderId() {
		return headerId;
	}
	public void setHeaderId(BigInteger headerId) {
		this.headerId = headerId;
	}
	public String getScheduledBy() {
		return scheduledBy;
	}
	public void setScheduledBy(String scheduledBy) {
		this.scheduledBy = scheduledBy;
	}
	public Timestamp getScheduledOn() {
		return scheduledOn;
	}
	public void setScheduledOn(Timestamp scheduledOn) {
		this.scheduledOn = scheduledOn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getActualRuntimeStart() {
		return actualRuntimeStart;
	}
	public void setActualRuntimeStart(Timestamp actualRuntimeStart) {
		this.actualRuntimeStart = actualRuntimeStart;
	}
	public Timestamp getActualRuntimeEnd() {
		return actualRuntimeEnd;
	}
	public void setActualRuntimeEnd(Timestamp actualRuntimeEnd) {
		this.actualRuntimeEnd = actualRuntimeEnd;
	}
	public Timestamp getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}
}
