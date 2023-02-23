package com.example.one_mw.collections.helper;

import java.util.HashMap;
import java.util.Map;

public class MapMerger {

	/*
	 * This function is designed to merge Maps of type <String,Object>
	 * In case a duplicate key is found it will be renamed i_Key where i=for_loop
	 */
	
	@SuppressWarnings("unchecked")
	public  Map<String,Object>  mergeMaps(Map<String,Object> ... maps){
		
		if(maps.length>0) {
			
			Map<String,Object> result = new HashMap<>();
			
			for(int i=0;i<maps.length;i++) {
				
				Map<String,Object> m = maps[i];
				
				for(Map.Entry<String, Object> entry:m.entrySet()) {
					
					if(result.containsKey(entry.getKey())) {
						
						String k=String.format("%d_%s", i,entry.getKey());
						result.put(k, entry.getValue());
					}
					else {
						
						result.put(entry.getKey(), entry.getValue());
					}
				}
				
			}
			
			return result;
		}
		
		return null;
	}
	
	public  Map<String,Object>  mergeHeaderAndLines(Map<String,Object> header,Map<String,Object> lines){
		
		Map<String,Object> result = new HashMap<>();
		
		result.putAll(header);
		
		result.put("Lines", lines);
		
		return result;
		
	}
}
