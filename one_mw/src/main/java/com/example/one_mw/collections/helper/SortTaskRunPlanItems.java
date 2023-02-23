package com.example.one_mw.collections.helper;

import java.util.Comparator;

import com.example.one_mw.entity.TaskRunItem;

public class SortTaskRunPlanItems  implements Comparator<TaskRunItem> {

	@Override
	public int compare(TaskRunItem arg0, TaskRunItem arg1) {
		
		try {
			
			return arg0.getRunOrder()-arg1.getRunOrder();
		}
		catch(Exception e) {
			
			return 99;
		}
		
		
	}

}
