package com.example.one_mw.collections.helper;

import java.util.Comparator;

import com.example.one_mw.entity.ServiceChecker;

public class SortRequestCheckers implements Comparator<ServiceChecker> {

	@Override
	public int compare(ServiceChecker o1, ServiceChecker o2) {

		try {
			
			return o1.getId()-o2.getId();
		}
		catch(Exception e) {
			
			return 99;
		}
	}

}
