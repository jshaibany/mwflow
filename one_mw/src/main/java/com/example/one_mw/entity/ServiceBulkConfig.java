package com.example.one_mw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="service_bulk_config")
public class ServiceBulkConfig {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="SERVICE_ID")	
	private Integer serviceId;
	@Column(name="REF_SERVICE_CODE")
	private String referenceServiceCode;
	@Column(name="REF_PROCESS_NAME")
	private String referenceProcessName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getReferenceServiceCode() {
		return referenceServiceCode;
	}
	public void setReferenceServiceCode(String referenceServiceCode) {
		this.referenceServiceCode = referenceServiceCode;
	}
	public String getReferenceProcessName() {
		return referenceProcessName;
	}
	public void setReferenceProcessName(String referenceProcessName) {
		this.referenceProcessName = referenceProcessName;
	}
}
