package com.example.one_mw.service.runtime;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.one_mw.collections.helper.SortRequestCheckers;
import com.example.one_mw.entity.ServiceBulkRequestChecker;
import com.example.one_mw.entity.ServiceChecker;
import com.example.one_mw.entity.ServiceRequestChecker;
import com.example.one_mw.service.RequestApprovalService;


@Service
public class RequestApprovalManager {

	Logger logger = LogManager.getLogger(RequestApprovalManager.class);
	
	@Autowired
	private RequestApprovalService requestApprovalServices;

	public String createApprovalList(BigInteger requestId,List<ServiceChecker> serviceCheckers) {
		
		logger.info(String.format("Try to create approval list .."));
		
		String currentApprover=null;
		
		Collections.sort(serviceCheckers, new SortRequestCheckers());
		
		for(ServiceChecker checker : serviceCheckers) {
			
			ServiceRequestChecker requestChecker = new ServiceRequestChecker();
			requestChecker.setRequestId(requestId);
			requestChecker.setRequestCheckerPrivilege(checker.getServiceCheckerPrivilege());
			requestChecker.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
			
			if(currentApprover==null) {
				
				logger.info(String.format("Set current approver [%s] ..",checker.getServiceCheckerPrivilege()));
				
				currentApprover=checker.getServiceCheckerPrivilege();
				requestChecker.setIsCurrentChecker(true);
			}
			
			logger.info(String.format("Create request checker in repo .."));
			requestApprovalServices.createRequestChecker(requestChecker);
		}
		
		return currentApprover;
	}
	
	public String createBulkApprovalList(BigInteger requestId,List<ServiceChecker> serviceCheckers) {
		
		logger.info(String.format("Try to create bulk approval list .."));
		
		String currentApprover=null;
		
		Collections.sort(serviceCheckers, new SortRequestCheckers());
		
		for(ServiceChecker checker : serviceCheckers) {
			
			ServiceBulkRequestChecker requestChecker = new ServiceBulkRequestChecker();
			requestChecker.setRequestId(requestId);
			requestChecker.setRequestCheckerPrivilege(checker.getServiceCheckerPrivilege());
			requestChecker.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
			
			if(currentApprover==null) {
				
				logger.info(String.format("Set current approver [%s] ..",checker.getServiceCheckerPrivilege()));
				
				currentApprover=checker.getServiceCheckerPrivilege();
				requestChecker.setIsCurrentChecker(true);
			}
			
			logger.info(String.format("Create bulk request checker in repo .."));
			requestApprovalServices.createRequestChecker(requestChecker);
		}
		
		return currentApprover;
	}
}
