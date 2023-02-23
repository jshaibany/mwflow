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
@Table(name="service_checker_def")
public class ServiceChecker {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="SERVICE_ID")	
	private Integer serviceId;
	@Column(name="SERVICE_CHECKER")
	private String serviceCheckerPrivilege;
	
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
	 * @return the serviceCheckerPrivilege
	 */
	public String getServiceCheckerPrivilege() {
		return serviceCheckerPrivilege;
	}

	/**
	 * @param serviceCheckerPrivilege the serviceCheckerPrivilege to set
	 */
	public void setServiceCheckerPrivilege(String serviceCheckerPrivilege) {
		this.serviceCheckerPrivilege = serviceCheckerPrivilege;
	}
}
