package com.example.one_mw.service;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.one_mw.entity.ServiceBulkRequestLine;
import com.example.one_mw.entity.ServiceRequestLine;
import com.example.one_mw.repository.ServiceBulkRequestLineRepository;
import com.example.one_mw.repository.ServiceRequestLineRepository;

@Service
public class ServiceRequestLineService {

	@Autowired
	private ServiceRequestLineRepository serviceRequestLineRepository;
	
	@Autowired
	private ServiceBulkRequestLineRepository serviceBulkRequestLineRepository;
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createNewRequestLine(ServiceRequestLine entity) {
		
		serviceRequestLineRepository.save(entity);
	}
	
	public List<ServiceRequestLine> getLinesByRequestId(BigInteger id){
		
		return serviceRequestLineRepository.findByRequestId(id);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createNewBulkRequestLine(ServiceBulkRequestLine entity) {
		
		serviceBulkRequestLineRepository.save(entity);
	}
	
	public List<ServiceBulkRequestLine> getBulkLinesByRequestId(BigInteger id){
		
		return serviceBulkRequestLineRepository.findByRequestId(id);
	}
}
