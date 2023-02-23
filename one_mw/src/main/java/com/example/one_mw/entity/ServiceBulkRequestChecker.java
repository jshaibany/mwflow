package com.example.one_mw.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="service_bulk_request_checker")
public class ServiceBulkRequestChecker {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private BigInteger id;	
	@Column(name="REQUEST_HEADER_ID")
	private BigInteger requestId;
	@Column(name="REQUEST_CHECKER")
	private String requestCheckerPrivilege;
	@Column(name="CHECKER_UPDATED_BY")
	private String updatedBy;
	@Column(name="CHECKER_ACTION")
	private String checkerAction;
	@Column(name="CHECKER_REMARKS")
	private String remarks;
	@Column(name="IS_CURRENT_CHECKER")
	private Boolean isCurrentChecker;
	@Column(name="CREATED_ON")
	private Timestamp createdOn;
	@Column(name="UPDATED_ON")
	private Timestamp updatedOn;
	
	@ManyToOne
	@JoinColumn(name="REQUEST_HEADER_ID",insertable=false, updatable=false)
	private ServiceBulkRequest serviceBulkRequest;
	
	/**
	 * @return the id
	 */
	public BigInteger getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}
	/**
	 * @return the requestId
	 */
	public BigInteger getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(BigInteger requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the requestCheckerPrivilege
	 */
	public String getRequestCheckerPrivilege() {
		return requestCheckerPrivilege;
	}
	/**
	 * @param requestCheckerPrivilege the requestCheckerPrivilege to set
	 */
	public void setRequestCheckerPrivilege(String requestCheckerPrivilege) {
		this.requestCheckerPrivilege = requestCheckerPrivilege;
	}
	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return the checkerAction
	 */
	public String getCheckerAction() {
		return checkerAction;
	}
	/**
	 * @param checkerAction the checkerAction to set
	 */
	public void setCheckerAction(String checkerAction) {
		this.checkerAction = checkerAction;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the isCurrentChecker
	 */
	public Boolean getIsCurrentChecker() {
		return isCurrentChecker;
	}
	/**
	 * @param isCurrentChecker the isCurrentChecker to set
	 */
	public void setIsCurrentChecker(Boolean isCurrentChecker) {
		this.isCurrentChecker = isCurrentChecker;
	}
	/**
	 * @return the createdOn
	 */
	public Timestamp getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the updatedOn
	 */
	public Timestamp getUpdatedOn() {
		return updatedOn;
	}
	/**
	 * @param updatedOn the updatedOn to set
	 */
	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}
}
