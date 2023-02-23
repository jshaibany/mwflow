package com.example.one_mw.entity;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="service_bulk_processing_report")
public class ServiceBulkProcessingReport {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="RESULT")	
	private Integer result;
	@Column(name="MESSAGE")	
	private String message;
	@Column(name="PROCESS_NO")	
	private BigInteger processNumber;
	@Column(name="HEADER_ID")	
	private BigInteger headerId;
	@Column(name="LINE_NO")	
	private Integer lineNumber;
	
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BigInteger getProcessNumber() {
		return processNumber;
	}
	public void setProcessNumber(BigInteger processNumber) {
		this.processNumber = processNumber;
	}
	public BigInteger getHeaderId() {
		return headerId;
	}
	public void setHeaderId(BigInteger headerId) {
		this.headerId = headerId;
	}
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
}
