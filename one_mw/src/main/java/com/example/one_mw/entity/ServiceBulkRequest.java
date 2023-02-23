package com.example.one_mw.entity;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="service_bulk_request_header")
public class ServiceBulkRequest {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="SERVICE_ID")	
	private Integer serviceId;
	@Column(name="SERVICE_NAME")
	private String serviceName;
	@Column(name="REQUEST_MAKER")
	private String serviceRequestMaker;
	@Column(name="REQUEST_BY")
	private String serviceRequestedBy;
	@Column(name="ACCOUNT_ID")
	private String requestAccountId;
	@Column(name="MSISDN")
	private String requestMsisdn;
	@Column(name="GROUP_ID")
	private String groupId;
	@Column(name="LAYER_ID")
	private String layerId;
	@Column(name="NEXT_CHECKER")
	private String nextChecker;
	@Column(name="LAST_CHECK_BY")
	private String lastCheckedBy;
	@Column(name="STATUS")
	private String status;
	@Column(name="CREATED_ON")
	private Timestamp createdOn;
	@Column(name="UPDATED_ON")
	private Timestamp updatedOn;
	
	@OneToMany(mappedBy="serviceBulkRequest",fetch = FetchType.EAGER)
	private List<ServiceBulkRequestLine> requestLines;
	
	@OneToMany(mappedBy="serviceBulkRequest",fetch = FetchType.LAZY)
	private List<ServiceBulkRequestChecker> requestCheckers;
	
	@Transient
	private ServiceBulkConfig serviceBulkConfig;
	
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
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return the serviceRequestMaker
	 */
	public String getServiceRequestMaker() {
		return serviceRequestMaker;
	}
	/**
	 * @param serviceRequestMaker the serviceRequestMaker to set
	 */
	public void setServiceRequestMaker(String serviceRequestMaker) {
		this.serviceRequestMaker = serviceRequestMaker;
	}
	/**
	 * @return the serviceRequestedBy
	 */
	public String getServiceRequestedBy() {
		return serviceRequestedBy;
	}
	/**
	 * @param serviceRequestedBy the serviceRequestedBy to set
	 */
	public void setServiceRequestedBy(String serviceRequestedBy) {
		this.serviceRequestedBy = serviceRequestedBy;
	}
	/**
	 * @return the requestAccountId
	 */
	public String getRequestAccountId() {
		return requestAccountId;
	}
	/**
	 * @param requestAccountId the requestAccountId to set
	 */
	public void setRequestAccountId(String requestAccountId) {
		this.requestAccountId = requestAccountId;
	}
	/**
	 * @return the requestMsisdn
	 */
	public String getRequestMsisdn() {
		return requestMsisdn;
	}
	/**
	 * @param requestMsisdn the requestMsisdn to set
	 */
	public void setRequestMsisdn(String requestMsisdn) {
		this.requestMsisdn = requestMsisdn;
	}
	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	/**
	 * @return the layerId
	 */
	public String getLayerId() {
		return layerId;
	}
	/**
	 * @param layerId the layerId to set
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}
	/**
	 * @return the nextChecker
	 */
	public String getNextChecker() {
		return nextChecker;
	}
	/**
	 * @param nextChecker the nextChecker to set
	 */
	public void setNextChecker(String nextChecker) {
		this.nextChecker = nextChecker;
	}
	/**
	 * @return the lastCheckedBy
	 */
	public String getLastCheckedBy() {
		return lastCheckedBy;
	}
	/**
	 * @param lastCheckedBy the lastCheckedBy to set
	 */
	public void setLastCheckedBy(String lastCheckedBy) {
		this.lastCheckedBy = lastCheckedBy;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	/**
	 * @return the requestLines
	 */
	public List<ServiceBulkRequestLine> getRequestLines() {
		return requestLines;
	}
	/**
	 * @param requestLines the requestLines to set
	 */
	public void setRequestLines(List<ServiceBulkRequestLine> requestLines) {
		this.requestLines = requestLines;
	}
	/**
	 * @return the requestCheckers
	 */
	public List<ServiceBulkRequestChecker> getRequestCheckers() {
		return requestCheckers;
	}
	/**
	 * @param requestCheckers the requestCheckers to set
	 */
	public void setRequestCheckers(List<ServiceBulkRequestChecker> requestCheckers) {
		this.requestCheckers = requestCheckers;
	}
	@Transient
	public ServiceBulkConfig getServiceBulkConfig() {
		return serviceBulkConfig;
	}
	
	public void setServiceBulkConfig(ServiceBulkConfig serviceBulkConfig) {
		this.serviceBulkConfig = serviceBulkConfig;
	}
}