package com.example.one_mw.collections.helper;

import java.util.Map;

public interface IListToMapConvertable<T> {

	public Map<String,T> getItemsAsMap();
}
