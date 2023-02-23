package com.example.one_mw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OneMwServiceInitializer {

	private final String oneMwUrl;
	private final String oneMwEndpointSubmitRequest;
	private final String oneMwEndpointStartBatchProcessor;
	 
	@Autowired
	OneMwServiceInitializer(@Value("${one_mw.url}") String oneMwUrl,
			@Value("${one_mw.endpoint.submit_request}") String oneMwEndpointSubmitRequest, 
			@Value("${one_mw.endpoint.start_batch_processor}") String oneMwEndpointStartBatchProcessor) {
	       
		this.oneMwUrl = oneMwUrl;
	    this.oneMwEndpointSubmitRequest = oneMwEndpointSubmitRequest;
		this.oneMwEndpointStartBatchProcessor = oneMwEndpointStartBatchProcessor;  
	   
	}

	public String getOneMwUrl() {
		return oneMwUrl;
	}

	public String getOneMwEndpointSubmitRequest() {
		return oneMwEndpointSubmitRequest;
	}

	public String getOneMwEndpointStartBatchProcessor() {
		return oneMwEndpointStartBatchProcessor;
	}
}
