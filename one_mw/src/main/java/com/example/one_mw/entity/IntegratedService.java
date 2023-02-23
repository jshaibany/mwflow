package com.example.one_mw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * .
 *
 * @author Galal M.Ahmed
 */

@Entity
@Table(name="integrated_service_def")
public class IntegratedService {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="NAME")
	private String name;
	@Column(name="TYPE_ID")
	private String type;
	@Column(name="TYPE_XML_FILE")
	private String typeXMLFile;
	@Column(name="METHOD")
	private String method;
	@Column(name="CRED_ID")
	private String credentialsId;
	@Column(name="CRED_XML_FILE")
	private String credentialsXMLFile;
	@Column(name="RESPONSE_TYPE")
	private String responseType;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeXMLFile() {
		return typeXMLFile;
	}
	public void setTypeXMLFile(String typeXMLFile) {
		this.typeXMLFile = typeXMLFile;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getCredentialsId() {
		return credentialsId;
	}
	public void setCredentialsId(String credentialsId) {
		this.credentialsId = credentialsId;
	}
	public String getCredentialsXMLFile() {
		return credentialsXMLFile;
	}
	public void setCredentialsXMLFile(String credentialsXMLFile) {
		this.credentialsXMLFile = credentialsXMLFile;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	
	
}
