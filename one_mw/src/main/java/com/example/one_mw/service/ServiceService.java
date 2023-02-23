package com.example.one_mw.service;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.one_mw.entity.ServiceChecker;
import com.example.one_mw.entity.ServiceSchemaItem;
import com.example.one_mw.repository.ServiceCheckerRepository;
import com.example.one_mw.repository.ServiceRepository;
import com.example.one_mw.repository.ServiceSchemaItemRepository;

@Service
public class ServiceService {

	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private ServiceSchemaItemRepository serviceSchemaItemRepository;
	
	@Autowired
	private ServiceCheckerRepository serviceCheckerRepository;
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createServiceDefinition(com.example.one_mw.entity.Service service) {
		
		serviceRepository.save(service);
	}
	
	public com.example.one_mw.entity.Service getServiceByCode(String code) {
		
		return serviceRepository.findByCode(code);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createServiceSchemaItem(ServiceSchemaItem item) {
		
		serviceSchemaItemRepository.save(item);
	}
	
	public Optional<ServiceSchemaItem> getAllServiceSchemaItems(Integer id) {
		
		return Optional.ofNullable(serviceSchemaItemRepository.findByServiceId(id));
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createServiceChecker(ServiceChecker entity) {
		
		serviceCheckerRepository.save(entity);
	}
	
	public Optional<ServiceChecker> getAllServiceCheckers(Integer id){
		
		return Optional.ofNullable(serviceCheckerRepository.findByServiceId(id));
	}
}
