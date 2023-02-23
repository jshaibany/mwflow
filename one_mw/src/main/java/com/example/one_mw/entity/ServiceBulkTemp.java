package com.example.one_mw.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="service_bulk_temp")
public class ServiceBulkTemp {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;	
	@Column(name="MSISDN")
	private String msisdn;
	@Column(name="REQUEST_BY")
	private String request_by;
	@Column(name="STARTED_ON")
	private Timestamp started_on;
	@Column(name="ENDED_ON")
	private Timestamp ended_on;
	@Column(name="TCS_RESPONSE")
	private String tcs_response;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getRequest_by() {
		return request_by;
	}
	public void setRequest_by(String request_by) {
		this.request_by = request_by;
	}
	public Timestamp getStarted_on() {
		return started_on;
	}
	public void setStarted_on(Timestamp started_on) {
		this.started_on = started_on;
	}
	public Timestamp getEnded_on() {
		return ended_on;
	}
	public void setEnded_on(Timestamp ended_on) {
		this.ended_on = ended_on;
	}
	public String getTcs_response() {
		return tcs_response;
	}
	public void setTcs_response(String tcs_response) {
		this.tcs_response = tcs_response;
	}
}
