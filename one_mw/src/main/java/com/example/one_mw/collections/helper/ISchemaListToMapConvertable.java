package com.example.one_mw.collections.helper;

import java.util.Map;

public interface ISchemaListToMapConvertable<T> {

	public Map<String,T> getSchemaItemsAsMap();
	public Map<String,T> getSchemaItemsAsMap(String Direction);
}
