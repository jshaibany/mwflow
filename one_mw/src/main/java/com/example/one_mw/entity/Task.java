package com.example.one_mw.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.one_mw.collections.helper.ISchemaListToMapConvertable;



@Entity
@Table(name="task_def")
public class Task implements ISchemaListToMapConvertable<ISchemaItem>{

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="NAME")
	private String name;
	@Column(name="DESCP")
	private String description;
	@Column(name="REQUEST_BODY")
	private String requestBody;
	@Column(name="INTEGRATED_SERVICE_ID")
	private Integer integratedServiceId;
	@Column(name="TYPE_ID")
	private String type;
	@Column(name="TYPE_XML_FILE")
	private String typeXMLFile;
	
	@OneToMany(mappedBy="task",fetch = FetchType.EAGER)
	private List<TaskSchemaItem> taskSchemaItems;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="INTEGRATED_SERVICE_ID",insertable=false, updatable=false)
	private IntegratedService taskService;
	
	@Transient
	private Process process;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
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

	public void setTypeXMLFile(String typeXmlFile) {
		this.typeXMLFile = typeXmlFile;
	}

	public List<TaskSchemaItem> getTaskSchemaItems() {
		return taskSchemaItems;
	}

	public void setTaskSchemaItems(List<TaskSchemaItem> taskSchemaItems) {
		this.taskSchemaItems = taskSchemaItems;
	}

	public IntegratedService getTaskService() {
		return taskService;
	}

	public void setTaskService(IntegratedService taskService) {
		this.taskService = taskService;
	}
	
	@Transient
	public Map<String, ISchemaItem> getTaskSchemaItemsAsMap(){
		
		Map<String,ISchemaItem> result = new HashMap<>();
		
		for(TaskSchemaItem item : taskSchemaItems) {
			result.put(item.getName(), item);
		}
		return result;
	}
	
	@Transient
	public Map<String, ISchemaItem> getTaskRequestSchemaItemsAsMap(){
		
		Map<String,ISchemaItem> result = new HashMap<>();
		
		for(TaskSchemaItem item : taskSchemaItems) {
			
			if(item.getItemDirection().contains("REQUEST"))
			result.put(item.getName(), item);
		}
		return result;
	}
	
	@Transient
	public Map<String, ISchemaItem> getTaskResponseSchemaItemsAsMap(){
		
		Map<String,ISchemaItem> result = new HashMap<>();
		
		for(TaskSchemaItem item : taskSchemaItems) {
			
			if(item.getItemDirection().contains("RESPONSE"))
			result.put(item.getName(), item);
		}
		return result;
	}

	@Transient
	@Override
	public Map<String, ISchemaItem> getSchemaItemsAsMap() {
		
		Map<String,ISchemaItem> result = new HashMap<>();
		
		for(TaskSchemaItem item : taskSchemaItems) {
			result.put(item.getName(), item);
		}
		return result;
	}

	@Transient
	@Override
	public Map<String, ISchemaItem> getSchemaItemsAsMap(String direction) {
		
		Map<String,ISchemaItem> result = new HashMap<>();
		
		for(TaskSchemaItem item : taskSchemaItems) {
			
			if(item.getItemDirection().contains(direction))
			result.put(item.getName(), item);
		}
		return result;
	}

	/**
	 * @return the process
	 */
	@Transient
	public Process getProcess() {
		return process;
	}

	/**
	 * @param process the process to set
	 */
	@Transient
	public void setProcess(Process process) {
		this.process = process;
	}

}
