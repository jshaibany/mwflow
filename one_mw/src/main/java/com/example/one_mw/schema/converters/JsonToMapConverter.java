package com.example.one_mw.schema.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonToMapConverter {

	public static Map<String, Object> convertJsonToFlattenedMap(String json) throws Exception{
		
		ObjectMapper mapper = new ObjectMapper();
	    JsonNode root = mapper.readTree(json);
	    Map<String, Object> map = new JsonToMapObjFlattner(root).flatten();
		
	    map.forEach(
	        (k, v) -> {
	          //System.out.println(k + " => " + v);
	        });
	    
	    return map;
	}
	
	public static Map<String, Object> convertJsonToMap(String json) throws Exception{
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		
		if(jsonNode.isArray()) {
			
			return convertJsonListToMap(json);
		}
		else {
			
			Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
		    });
			
		    map.forEach(
		        (k, v) -> {
		          //System.out.println(k + " => " + v);
		        });
		    
		    return map;
		}
	    
	}
	
	private static Map<String, Object> convertJsonListToMap(String json) throws Exception{
		
		ObjectMapper mapper = new ObjectMapper();
	    List<Object> list = mapper.readValue(json, new TypeReference<List<Object>>() {
	    });
		
	    Map<String, Object> map = new HashMap<>();
	    
	    map.put("lines", list);
	    
	    map.forEach(
	        (k, v) -> {
	          //System.out.println(k + " => " + v);
	        });
	    
	    return map;
	}
	
	public static Map<String, String> convertNodesFromJson(String json) throws Exception{
		
		ObjectMapper mapper = new ObjectMapper();
	    JsonNode root = mapper.readTree(json);
	    Map<String, JsonNode> map = new JsonToMapFlattner(root).flatten();
		Map<String,String> result = new HashMap<>();
		
	    map.forEach(
	        (k, v) -> {
	          //System.out.println(k + " => " + v);
	          result.put(k, v.asText());
	        });
	    
	    return result;
	}
	
	public static Map<String, Object> convertNodesFromJsonObj(String json) throws Exception{
		
		ObjectMapper mapper = new ObjectMapper();
	    JsonNode root = mapper.readTree(json);
	    Map<String, Object> map = new JsonToMapObjFlattner(root).flatten();
		
	    map.forEach(
	        (k, v) -> {
	          //System.out.println(k + " => " + v);
	        });
	    
	    return map;
	}
}
