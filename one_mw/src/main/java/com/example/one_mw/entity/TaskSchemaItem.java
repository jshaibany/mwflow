package com.example.one_mw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="task_schema_item")
public class TaskSchemaItem implements ISchemaItem{

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="TASK_ID")	
	private Integer taskId;
	@Column(name="NAME")
	private String name;
	@Column(name="DESCP")
	private String description;
	@Column(name="MAP_OF")
	private String mapOf;
	@Column(name="DATA_TYPE")
	private String dataType;
	@Column(name="NILLABLE")
	private Boolean isNillable;
	@Column(name="DEFAULT_VALUE")
	private String defaultValue;
	@Column(name="EXPRESSION")
	private String expression;
	@Column(name="PERSISTABLE")
	private Boolean isPersistable;
	@Column(name="DIRECTION")
	private String itemDirection;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="TASK_ID",insertable=false, updatable=false)
	private Task task;

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getMapOf() {
		return mapOf;
	}

	public void setMapOf(String mapOf) {
		this.mapOf = mapOf;
	}

	@Override
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Override
	public Boolean getIsNillable() {
		return isNillable;
	}

	public void setIsNillable(Boolean isNillable) {
		this.isNillable = isNillable;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Override
	public Boolean getIsPersistable() {
		return isPersistable;
	}

	public void setIsPersistable(Boolean isPersistable) {
		this.isPersistable = isPersistable;
	}

	@Override
	public String getItemDirection() {
		return itemDirection;
	}

	public void setItemDirection(String itemDirection) {
		this.itemDirection = itemDirection;
	}
	
}
