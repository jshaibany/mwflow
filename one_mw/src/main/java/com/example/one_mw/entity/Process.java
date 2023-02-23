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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="process_def")
public class Process{

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="NAME")
	private String name;
	@Column(name="DESCP")
	private String description;
	@Column(name="TYPE_ID")
	private String type;
	@Column(name="TYPE_XML_FILE")
	private String typeXMLFile;
	
	@OneToMany(mappedBy="process",fetch = FetchType.EAGER)
	private List<TaskRunItem> tasksRunPlan;
	
	@Transient
	private Map<String,Object> processRunRepos;
	
	@Transient
	private Map<String,Object> inMap;
	
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
	public List<TaskRunItem> getTasksRunPlan() {
		return tasksRunPlan;
	}
	public void setTasksRunPlan(List<TaskRunItem> tasksRunPlan) {
		this.tasksRunPlan = tasksRunPlan;
	}
	
	@Transient
	public Map<String,TaskRunItem> getTasksRunPlanAsMap(){
		
		Map<String,TaskRunItem> result = new HashMap<>();
		for(TaskRunItem item : tasksRunPlan) {
			result.put(item.getTaskName(), item);
		}
		
		return result;
	}
	
	@Transient
	public Map<String,Object> getProcessRunRepos() {
		return processRunRepos;
	}
	
	@Transient
	public void setProcessRunRepos(Map<String,Object> processRunRepos) {
		this.processRunRepos = processRunRepos;
	}
	
	@Transient
	public Map<String,Object> getInMap() {
		return inMap;
	}
	
	@Transient
	public void setInMap(Map<String,Object> inMap) {
		this.inMap = inMap;
	}
	
	
}
