package com.example.one_mw.service;

import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OneMwEndpointsService {

	public String submitRequest(String jsonRequest,String url,String endpoint){
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<String>(jsonRequest, headers);
		ResponseEntity<String> response = new RestTemplate()
				.postForEntity(url+endpoint, 
						request, 
						String.class);
	    
		String result= response.getBody();
		
		return result;
		
	}

}
