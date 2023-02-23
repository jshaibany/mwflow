package com.example.one_mw.collections.helper;

import java.util.Comparator;

import com.example.one_mw.entity.ServiceDecisionLine;

public class SortServiceDecisionLinePriority implements Comparator<ServiceDecisionLine> {

	@Override
	public int compare(ServiceDecisionLine arg0, ServiceDecisionLine arg1) {
		
		try {
			
			return arg0.getPriority()-arg1.getPriority();
		}
		catch(Exception e) {
			
			return 99;
		}
		
		
	}
}
