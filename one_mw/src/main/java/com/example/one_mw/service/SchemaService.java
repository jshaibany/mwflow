package com.example.one_mw.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.example.one_mw.entity.ISchemaItem;
import com.example.one_mw.exception.InvalidMapSchemaException;
import com.example.one_mw.exception.MapExpressionEvaluationException;


@Service
public class SchemaService {

	Logger logger = LogManager.getLogger(SchemaService.class);
	
	public void verifyParamNamesCompliance(Map<String,Object> inMap,Map<String,ISchemaItem> schemaItems) throws InvalidMapSchemaException {
				
		try {
			
			logger.info(String.format("Verify parameter names compliance .."));
			
			for(Map.Entry<String, ISchemaItem> item: schemaItems.entrySet()) {
				
				if(item.getValue().getMapOf()!=null && inMap.get(item.getValue().getMapOf())==null && !item.getValue().getIsNillable()) {
					
					//A MapOf existed and no match in the inMap and the param is not Nillable
					throw new InvalidMapSchemaException(String.format("InvalidMapSchemaException, Function verifyParamNamesCompliance, Item %s ",item.getKey()));
				}

			}
		}
		catch(Exception e) {
			
			logger.error(String.format("Verify parameter names compliance error .."));
			logger.error(e.getMessage());
			
			e.printStackTrace();
			throw new InvalidMapSchemaException(String.format("InvalidMapSchemaException, Function verifyParamNamesCompliance "));
		}
	}
	
	public Map<String,Object> convertToSchemaMap(Map<String,Object> inMap,Map<String,ISchemaItem> schemaItems) 
			throws InvalidMapSchemaException, 
				   MapExpressionEvaluationException{
		
		logger.info(String.format("Convert parameters to schema map .."));
		
		if(schemaItems != null && schemaItems.size()>0) {
			
			logger.info(String.format("Schema items found, start to convert:"));
			
			//1-Verify the param names are existed
			
			verifyParamNamesCompliance(inMap,schemaItems);
			
			Map<String,Object> result = new HashMap<>();
			
			for (Map.Entry<String,ISchemaItem> entry : schemaItems.entrySet()) {
				
				ISchemaItem schema = entry.getValue();
				
				//2-For each item qualify the Object type and match it with the schema type
				
				if(inMap.get(schema.getMapOf())!=null) {
					
					logger.info(String.format("Qualify [%s] with Data Type [%s]..",schema.getMapOf(),schema.getDataType()));
					
					if(!LanguageExpressionService.qualifyObjectDataType(inMap.get(schema.getMapOf()),schema.getDataType())) {
						
						logger.error(String.format("[%s] Not Qualified ..",schema.getMapOf()));
						throw new InvalidMapSchemaException(String.format("Schema Item %s is Invalid ...",schema.getName()));
					}
					
					logger.info(String.format("[%s] Qualified successfully ..",schema.getMapOf()));
				}
				
				//Check if item has expression to be evaluated
				
				//If no expression in place, try to map directly from the inMap
				
				//If the direct map failed, check if 
				
				//If no default value, check if the param is nillable then put it NULL
				
				//Finally raise an exception
				
				if(schema.getExpression() != null && !schema.getExpression().isEmpty()) {
					
					logger.info(String.format("Evaluate [%s] using expression [%s]..",schema.getName(),schema.getExpression()));
					
					//Check if item has expression to be evaluated
				
					result.put(schema.getName(), LanguageExpressionService.evaluateMapExpressionToObject(inMap, schema.getExpression()));
				}
				else {
					
					if(inMap.get(schema.getMapOf())!=null && !inMap.get(schema.getMapOf()).toString().isEmpty()) {
						
						logger.info(String.format("Evaluate [%s] using mapping of inMap param [%s]..",schema.getName(),schema.getMapOf()));
						
						//If no expression in place, try to map directly from the inMap
						result.put(schema.getName(), inMap.get(schema.getMapOf()));
					}
					else {
						
						//If the direct map failed, check if
						if(schema.getDefaultValue()!=null && !schema.getDefaultValue().isEmpty()) {
							
							logger.info(String.format("Evaluate [%s] using default value [%s]..",schema.getName(),schema.getDefaultValue().toString()));
							
							result.put(schema.getName(), schema.getDefaultValue());
						}
						else {
							
							if(schema.getIsNillable()) {
								
								logger.info(String.format("Evaluate [%s] as NULL ..",schema.getName()));
								//If no default value, check if the param is nillable then put it NULL
								result.put(schema.getName(), null);
							}
							else {
								
								logger.info(String.format("Evaluate [%s] has failed to find a valuation option ..",schema.getName()));
								throw new InvalidMapSchemaException(String.format("All alternatives have failed to match schema item [%s]", schema.getName()));
							}
						}
					}
				}
			}
			
			return result;
		}
		else {
			
			logger.info(String.format("Schema items not found, return inMap values:"));
			return inMap;
		}
		
	}
	
}
