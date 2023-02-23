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
@Table(name="service_schema_item")
public class ServiceSchemaItem implements ISchemaItem {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="SERVICE_ID")	
	private Integer serviceId;
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
	
	@ManyToOne
	@JoinColumn(name="SERVICE_ID",insertable=false, updatable=false)
	private Service service;
	/**
	 * @return the id
	 */
	@Override
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
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the mapOf
	 */
	@Override
	public String getMapOf() {
		return mapOf;
	}
	/**
	 * @param mapOf the mapOf to set
	 */
	public void setMapOf(String mapOf) {
		this.mapOf = mapOf;
	}
	/**
	 * @return the dataType
	 */
	@Override
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the isNillable
	 */
	@Override
	public Boolean getIsNillable() {
		return isNillable;
	}
	/**
	 * @param isNillable the isNillable to set
	 */
	public void setIsNillable(Boolean isNillable) {
		this.isNillable = isNillable;
	}
	/**
	 * @return the defaultValue
	 */
	@Override
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @return the expression
	 */
	@Override
	public String getExpression() {
		return expression;
	}
	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}
	/**
	 * @return the isPersistable
	 */
	@Override
	public Boolean getIsPersistable() {
		return isPersistable;
	}
	/**
	 * @param isPersistable the isPersistable to set
	 */
	public void setIsPersistable(Boolean isPersistable) {
		this.isPersistable = isPersistable;
	}
	/**
	 * @return the itemDirection
	 */
	@Override
	public String getItemDirection() {
		return itemDirection;
	}
	/**
	 * @param itemDirection the itemDirection to set
	 */
	public void setItemDirection(String itemDirection) {
		this.itemDirection = itemDirection;
	}
	
	
	
}
