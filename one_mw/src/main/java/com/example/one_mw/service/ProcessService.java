package com.example.one_mw.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.one_mw.entity.Process;
import com.example.one_mw.repository.ProcessRepository;


@Service
public class ProcessService {

	@Autowired
	private ProcessRepository processRepository;
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createProcessDefinition(com.example.one_mw.entity.Process process) {
		
		processRepository.save(process);
	}
	
	public Process getProcessByName(String name){
		
		return processRepository.findByName(name);
	}
	
}
