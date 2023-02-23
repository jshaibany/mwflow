package com.example.one_mw.helper;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DataTypeQulifier {

	public Boolean isInstanceOf(Object o, String className) {
		
		switch(className) {
		
		case"String":
		case"Integer":
		case"Boolean":
		case"Long":
		className="java.lang."+className;
			try {
			
				Class<?> clazz;
				clazz = Class.forName(className);
				return clazz.isInstance(o);
			
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
				return false;
			}
	
		case"BigInteger":
			try {
				
				BigInteger.valueOf(Integer.valueOf(o.toString()));
				
				return true;
				
			} catch (Exception e) {
				
				e.printStackTrace();
				return false;
			}
		case"BigDecimal":
		case"Float":
		case"Double":
			
			try {
				
				BigDecimal.valueOf(Double.valueOf(o.toString()));
				
				return true;
				
			} catch (Exception e) {
				
				e.printStackTrace();
				return false;
			}
			default:
				return false;
			
		}
		
		
		
	}
}
