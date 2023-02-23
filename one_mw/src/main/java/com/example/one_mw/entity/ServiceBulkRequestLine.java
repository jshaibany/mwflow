package com.example.one_mw.entity;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="service_bulk_request_line")
public class ServiceBulkRequestLine {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private BigInteger id;	
	@Column(name="REQUEST_HEADER_ID")
	private BigInteger requestId;
	@Column(name="LINE_NO")
	private Integer lineNumber;
	@Column(name="PARAM_NAME")
	private String paramName;
	@Column(name="PARAM_VALUE")
	private String paramValue;
	@Column(name="PARAM_SOURCE")
	private String paramSource;
	
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
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	/**
	 * @return the paramName
	 */
	public String getParamName() {
		return paramName;
	}
	/**
	 * @param paramName the paramName to set
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	/**
	 * @return the paramValue
	 */
	public String getParamValue() {
		return paramValue;
	}
	/**
	 * @param paramValue the paramValue to set
	 */
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	/**
	 * @return the paramSource
	 */
	public String getParamSource() {
		return paramSource;
	}
	/**
	 * @param paramSource the paramSource to set
	 */
	public void setParamSource(String paramSource) {
		this.paramSource = paramSource;
	}
}
