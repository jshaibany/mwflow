package com.example.one_mw.service;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.one_mw.entity.ServiceSchemaItem;
import com.example.one_mw.repository.ServiceSchemaItemRepository;

@Service
public class ServiceSchemaItemService {

	@Autowired
	private ServiceSchemaItemRepository serviceSchemaItemRepository;
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createServiceSchemaItem(ServiceSchemaItem item) {
		
		serviceSchemaItemRepository.save(item);
	}
	
	public Optional<ServiceSchemaItem> getAllServiceSchemaItems(Integer id) {
		
		return Optional.ofNullable(serviceSchemaItemRepository.findByServiceId(id));
	}
}
