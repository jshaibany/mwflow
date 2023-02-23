package com.example.one_mw.entity;

import java.util.Collections;
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

import com.example.one_mw.collections.helper.ISchemaListToMapConvertable;
import com.example.one_mw.collections.helper.SortServiceDecisionLinePriority;

@Entity
@Table(name="service_def")
public class Service implements ISchemaListToMapConvertable<ISchemaItem>{

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="CODE")
	private String code;
	@Column(name="NAME")
	private String name;
	@Column(name="DESCP")
	private String description;
	@Column(name="TYPE")
	private String type;
	@Column(name="SERVICE_MAKER")
	private String serviceMaker;
	@Column(name="IS_ACTIVE")
	private Boolean isActive;
	@Column(name="IS_MAKER_CHECKER")
	private Boolean isMakerCheckerEnabled;
	@Column(name="IS_BATCH_ENABLED")
	private Boolean isBatchProcessingEnabled;
	@Column(name="IS_CHECKONLY_ENABLED")
	private Boolean isCheckOnlyRunEnabled;
	@Column(name="JSON_TEMPLATE")
	private String jsonTemplate;
	
	@OneToMany(mappedBy="service",fetch = FetchType.EAGER)
	private List<ServiceSchemaItem> serviceSchemaItems;
	
	@OneToMany(mappedBy="service",fetch = FetchType.LAZY)
	private List<ServiceChecker> serviceCheckers;
	
	@OneToMany(mappedBy="service",fetch = FetchType.LAZY)
	private List<ServiceDecisionLine> serviceDecisionTable;
	
	@Transient
	private Map<String,Object> processSchemaItems;
	
	@Transient
	public String processName;
	
	/**
	 * @return the processSchemaItems
	 */
	@Transient
	public Map<String, Object> getProcessSchemaItems() {
		return processSchemaItems;
	}
	/**
	 * @param processSchemaItems the processSchemaItems to set
	 */
	@Transient
	public void setProcessSchemaItems(Map<String, Object> processSchemaItems) {
		this.processSchemaItems = processSchemaItems;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getServiceMaker() {
		return serviceMaker;
	}
	public void setServiceMaker(String serviceMaker) {
		this.serviceMaker = serviceMaker;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsMakerCheckerEnabled() {
		return isMakerCheckerEnabled;
	}
	public void setIsMakerCheckerEnabled(Boolean isMakerCheckerEnabled) {
		this.isMakerCheckerEnabled = isMakerCheckerEnabled;
	}
	/**
	 * @return the jsonTemplate
	 */
	public String getJsonTemplate() {
		return jsonTemplate;
	}
	/**
	 * @param jsonTemplate the jsonTemplate to set
	 */
	public void setJsonTemplate(String jsonTemplate) {
		this.jsonTemplate = jsonTemplate;
	}
	public Boolean getIsBatchProcessingEnabled() {
		return isBatchProcessingEnabled;
	}
	public void setIsBatchProcessingEnabled(Boolean isBatchProcessingEnabled) {
		this.isBatchProcessingEnabled = isBatchProcessingEnabled;
	}
	public Boolean getIsCheckOnlyRunEnabled() {
		return isCheckOnlyRunEnabled;
	}
	public void setIsCheckOnlyRunEnabled(Boolean isCheckOnlyRunEnabled) {
		this.isCheckOnlyRunEnabled = isCheckOnlyRunEnabled;
	}
	
	/**
	 * @return the serviceCheckers
	 */
	public List<ServiceChecker> getServiceCheckers() {
		return serviceCheckers;
	}
	/**
	 * @param serviceCheckers the serviceCheckers to set
	 */
	public void setServiceCheckers(List<ServiceChecker> serviceCheckers) {
		this.serviceCheckers = serviceCheckers;
	}
	
	@Transient
	@Override
	public Map<String, ISchemaItem> getSchemaItemsAsMap() {

		Map<String, ISchemaItem> result = new HashMap<>();
		
		for(ServiceSchemaItem i : serviceSchemaItems) {
			
			result.put(i.getName(), i);
		}
		
		return result;
	}
	
	@Transient
	@Override
	public Map<String, ISchemaItem> getSchemaItemsAsMap(String direction) {
		
		Map<String,ISchemaItem> result = new HashMap<>();
		
		for(ServiceSchemaItem item : serviceSchemaItems) {
			
			if(item.getItemDirection().contains(direction))
			result.put(item.getName(), item);
		}
		return result;
	}
	/**
	 * @return the serviceDecisionTable
	 */
	public List<ServiceDecisionLine> getServiceDecisionTable() {
		
		Collections.sort(serviceDecisionTable,new SortServiceDecisionLinePriority());
		return serviceDecisionTable;
	}
	/**
	 * @param serviceDecisionTable the serviceDecisionTable to set
	 */
	public void setServiceDecisionTable(List<ServiceDecisionLine> serviceDecisionTable) {
		
		this.serviceDecisionTable = serviceDecisionTable;
	}
	
	
	
}
