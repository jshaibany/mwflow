package com.example.one_mw.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.example.one_mw.entity.Task;
import com.example.one_mw.exception.MapExpressionEvaluationException;
import com.example.one_mw.exception.ProcessExpressionEvaluationException;
import com.example.one_mw.exception.TaskExpressionEvaluationException;
import com.example.one_mw.helper.DataTypeQulifier;
import com.example.one_mw.helper.ProcessRootObjectContainer;



public class LanguageExpressionService {

	static Logger logger = LogManager.getLogger(LanguageExpressionService.class);
	
	public static Boolean qualifyObjectDataType(Object o,String className) {
		
		//logger.trace(String.format("Qualify Object of Type [%s] into Type [%s], Object Value=[%s] .. ", o.getClass().getName(),className,o.toString()));
		
		// Create or retrieve an engine
	    JexlEngine jexl = new JexlBuilder().create();
	    
	    DataTypeQulifier q= new DataTypeQulifier();
	    
	    // Create an expression
	    
	    String jexlExp = String.format("q.isInstanceOf(o,\"%s\")", className);
	    JexlExpression e = jexl.createExpression( jexlExp );
	    
	    
	    // Create a context and add data
	    JexlContext jc = new MapContext();
	    jc.set("o", o );
	    jc.set("q", q );
	    
	    
	    // Now evaluate the expression, getting the result
	    return (Boolean) e.evaluate(jc);
	}
	
	public static String evaluateMapExpressionToString(Map<String,String> map,String expression) throws MapExpressionEvaluationException {
		
		try {
			
			logger.trace(String.format("Evaluate expression [%s] .. ",expression));
			
			map.forEach((k,v)->{
				
				if(!k.contains("password") && v!=null)
				logger.trace(String.format("Input Map, Key=[%s], Value=[%s]", k,v.toString()));
			});
			
			// Create or retrieve an engine
		    JexlEngine jexl = new JexlBuilder().create();
		    
		    // Create an expression
		    String jexlExp = expression;
		    JexlExpression e = jexl.createExpression( jexlExp );
		    
		    // Create a context and add data
		    JexlContext jc = new MapContext();
		    jc.set("map", map );
		    
		    // Now evaluate the expression, getting the result
		    return (String) e.evaluate(jc);
		}
		catch(Exception e) {
			
			e.printStackTrace();
			throw new MapExpressionEvaluationException(String.format("Function: evaluateMapExpressionToString, Expression:%s", expression));
			
		}
		
	    
	}
	
    public static Object evaluateMapExpressionToObject(Map<String,Object> map,String expression) throws MapExpressionEvaluationException {
		
    	//Used in SchemaService.convertToServiceSchemaMap
    	
		try {
			
			logger.trace(String.format("Evaluate expression [%s] .. ",expression));
			
			map.forEach((k,v)->{
				
				if(!k.contains("password") && v!=null)
				logger.trace(String.format("Input Map, Key=[%s], Value=[%s]", k,v.toString()));
			});
			
			// Create or retrieve an engine
		    JexlEngine jexl = new JexlBuilder().create();
		    
		    // Create an expression
		    String jexlExp = expression;
		    JexlExpression e = jexl.createExpression( jexlExp );
		    
		    // Create a context and add data
		    JexlContext jc = new MapContext();
		    
		    jc.set("JSON", createEvaluationContext("JSON"));
		    jc.set("merger", createEvaluationContext("merger"));
		    jc.set("map", map );
		    
		    // Now evaluate the expression, getting the result
		    return e.evaluate(jc);
		}
		catch(Exception e) {
			
			e.printStackTrace();
			throw new MapExpressionEvaluationException(String.format("Function: evaluateMapExpressionToObject, Expression:%s", expression));
			
		}
		
	    
	}
    
    public static Boolean evaluateLogicalExpression(Map<String,Object> map,String expression) throws MapExpressionEvaluationException {
		
    	//Used for general logical expressions
    	
		try {
			
			logger.trace(String.format("Evaluate expression [%s] .. ",expression));
			
			map.forEach((k,v)->{
				
				if(!k.contains("password") && v!=null)
				logger.trace(String.format("Input Map, Key=[%s], Value=[%s]", k,v.toString()));
			});
			
			// Create or retrieve an engine
		    JexlEngine jexl = new JexlBuilder().create();
		    
		    // Create an expression
		    String jexlExp = expression;
		    JexlExpression e = jexl.createExpression( jexlExp );
		    
		    // Create a context and add data
		    JexlContext jc = new MapContext();
		    jc.set("map", map );
		    
		    // Now evaluate the expression, getting the result
		    Object o= e.evaluate(jc);
		    
		    if(o instanceof Boolean) {
		    	
		    	return (Boolean) o;
		    }
		    else {
		    	
		    	throw new MapExpressionEvaluationException(String.format("Function: evaluateLogicalExpression, Expression:%s", expression));
		    	
		    }
		}
		catch(Exception e) {
			
			e.printStackTrace();
			throw new MapExpressionEvaluationException(String.format("Function: evaluateLogicalExpression, Expression:%s", expression));
			
		}
		
	    
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,String> evaluateProcessExpressionToMap(Process process,String expression) throws ProcessExpressionEvaluationException {
		
		try {
			
			logger.trace(String.format("Evaluate expression [%s] .. ",expression));
			
			// Create or retrieve an engine
		    JexlEngine jexl = new JexlBuilder().create();
		    
		    // Create an expression
		    String jexlExp = expression;
		    JexlExpression e = jexl.createExpression( jexlExp );
		    
		    // Create a context and add data
		    JexlContext jc = new MapContext();
		    jc.set("process", process );
		    
		    // Now evaluate the expression, getting the result
		    return (Map<String,String>) e.evaluate(jc);
		}
		catch(Exception e) {
			
			e.printStackTrace();
			throw new ProcessExpressionEvaluationException(String.format("Function: evaluateProcessExpressionToMap, Expression:%s", expression));
			
		}
		
	    
	}
	
	public static String evaluateProcessExpressionToString(Process process,String expression) throws ProcessExpressionEvaluationException {
		
		try {
			
			logger.trace(String.format("Evaluate expression [%s] .. ",expression));
			
			// Create or retrieve an engine
		    JexlEngine jexl = new JexlBuilder().create();
		    
		    // Create an expression
		    String jexlExp = expression;
		    JexlExpression e = jexl.createExpression( jexlExp );
		    
		    // Create a context and add data
		    JexlContext jc = new MapContext();
		    jc.set("process", process );
		    
		    // Now evaluate the expression, getting the result
		    return (String) e.evaluate(jc);
		}
		catch(Exception e) {
			
			e.printStackTrace();
			throw new ProcessExpressionEvaluationException(String.format("Function: evaluateProcessExpressionToString, Expression:%s", expression));
			
		}
		
	    
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,String> evaluateTaskExpressionToMap(Task task,String expression) throws TaskExpressionEvaluationException {
		
		try {
			
			logger.trace(String.format("Evaluate expression [%s] .. ",expression));
			
			// Create or retrieve an engine
		    JexlEngine jexl = new JexlBuilder().create();
		    
		    // Create an expression
		    String jexlExp = expression;
		    JexlExpression e = jexl.createExpression( jexlExp );
		    
		    // Create a context and add data
		    JexlContext jc = new MapContext();
		    jc.set("task", task );
		    
		    // Now evaluate the expression, getting the result
		    return (Map<String,String>) e.evaluate(jc);
		}
		catch(Exception e) {
			
			e.printStackTrace();
			throw new TaskExpressionEvaluationException(String.format("Function: evaluateTaskExpressionToMap, Expression:%s", expression));
			
		}
		
	    
	}
	
	public static String evaluateTaskExpressionToString(Task task,String expression) throws TaskExpressionEvaluationException {
		
		try {
			
			logger.trace(String.format("Evaluate expression [%s] .. ",expression));
			
			// Create or retrieve an engine
		    JexlEngine jexl = new JexlBuilder().create();
		    
		    // Create an expression
		    String jexlExp = expression;
		    JexlExpression e = jexl.createExpression( jexlExp );
		    
		    // Create a context and add data
		    JexlContext jc = new MapContext();
		    jc.set("task", task );
		    
		    // Now evaluate the expression, getting the result
		    return (String) e.evaluate(jc);
		}
		catch(Exception e) {
			
			e.printStackTrace();
			throw new TaskExpressionEvaluationException(String.format("Function: evaluateTaskExpressionToString, Expression:%s", expression));
			
		}
		
	    
	}

	public static Object evaluateMapExpressionToObject(ProcessRootObjectContainer root,String expression,List<String> beans) throws MapExpressionEvaluationException {
		
    	/*
    	 * This function takes a root Map, String expression & a list of bean names to be created and used 
    	 * in expression evaluation
    	 * 
    	 * This function is an overload of evaluateMapExpressionToObject
    	 */
    	
		try {
			
			logger.trace(String.format("Evaluate expression [%s] .. ",expression));
			
			// Create or retrieve an engine
		    JexlEngine jexl = new JexlBuilder().create();
		    
		    // Create an expression
		    String jexlExp = expression;
		    JexlExpression e = jexl.createExpression( jexlExp );
		    
		    // Create a context and add data
		    JexlContext jc = new MapContext();
		    
		    if(beans != null && beans.size()>0) {
		    	
		    	for(String beanName : beans) {
		    		
		    		jc.set(beanName, createEvaluationContext(beanName) );
		    	}
		    }
		   
		    jc.set("root", root );
		    
		    // Now evaluate the expression, getting the result
		    return e.evaluate(jc);
		}
		catch(Exception e) {
			
			e.printStackTrace();
			throw new MapExpressionEvaluationException(String.format("Function: evaluateMapExpressionToObject, Expression:%s", expression));
			
		}
		
	    
	}
	
	private static Object createEvaluationContext(String beanName) {
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("ProcessBeans.xml");
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));
		
		Object bean = parser.parseExpression("@"+beanName).getValue(context);
		
		return bean;
	}
}
