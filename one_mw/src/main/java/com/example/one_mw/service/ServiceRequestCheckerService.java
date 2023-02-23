package com.example.one_mw.service;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.one_mw.entity.ServiceBulkRequestChecker;
import com.example.one_mw.entity.ServiceRequestChecker;
import com.example.one_mw.repository.ServiceBulkRequestCheckerRepository;
import com.example.one_mw.repository.ServiceRequestCheckerRepository;

@Service
public class ServiceRequestCheckerService {

	@Autowired
	private ServiceRequestCheckerRepository serviceRequestCheckerRepository;
	
	@Autowired
	private ServiceBulkRequestCheckerRepository serviceBulkRequestCheckerRepository;
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createNewRequestChecker(ServiceRequestChecker entity) {
		
		serviceRequestCheckerRepository.save(entity);
	}
	
	public List<ServiceRequestChecker> getRequestCheckersByRequestId(BigInteger id){
		
		return serviceRequestCheckerRepository.findByRequestId(id);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createNewBulkRequestChecker(ServiceBulkRequestChecker entity) {
		
		serviceBulkRequestCheckerRepository.save(entity);
	}
	
	public List<ServiceBulkRequestChecker> getBulkRequestCheckersByRequestId(BigInteger id){
		
		return serviceBulkRequestCheckerRepository.findByRequestId(id);
	}
}
