package com.example.one_mw.entity;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="service_bulk_processing_safe")
public class ServiceBulkProcessingSafe {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="SCHEDULE_ID")	
	private BigInteger scheduleId;
	@Column(name="HEADER_ID")	
	private BigInteger headerId;
	@Column(name="USERNAME")	
	private String username;
	@Column(name="PASSWORD")	
	private String password;
	@Column(name="USER_ID")	
	private String userId;
	@Column(name="TERMINAL_TYPE")	
	private String terminalType;
	
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public BigInteger getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(BigInteger scheduleId) {
		this.scheduleId = scheduleId;
	}
	public BigInteger getHeaderId() {
		return headerId;
	}
	public void setHeaderId(BigInteger headerId) {
		this.headerId = headerId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTerminalType() {
		return terminalType;
	}
	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
	
	
}
