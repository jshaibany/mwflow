package com.example.one_mw.entity;

public interface ISchemaItem {

	public Integer getId();
	public String getName();
	public String getDescription();
	public String getMapOf();
	public String getDataType();
	public Boolean getIsNillable();
	public String getDefaultValue();
	public String getExpression();
	public Boolean getIsPersistable();
	public String getItemDirection();
}
