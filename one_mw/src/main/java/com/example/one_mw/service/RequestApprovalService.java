package com.example.one_mw.service;



import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.one_mw.entity.ServiceBulkRequestChecker;
import com.example.one_mw.entity.ServiceRequestChecker;
import com.example.one_mw.repository.ServiceBulkRequestCheckerRepository;
import com.example.one_mw.repository.ServiceRequestCheckerRepository;

@Service
public class RequestApprovalService {

	//put the required repos to control request checkers and approval actions
	@Autowired
	private ServiceRequestCheckerRepository serviceRequestCheckerRepository;
	
	@Autowired
	private ServiceBulkRequestCheckerRepository serviceBulkRequestCheckerRepository;
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createRequestChecker(ServiceRequestChecker requestChecker) {
		
		serviceRequestCheckerRepository.save(requestChecker);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createRequestChecker(ServiceBulkRequestChecker requestChecker) {
		
		serviceBulkRequestCheckerRepository.save(requestChecker);
	}
	
}
