package com.example.one_mw.service;



import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.one_mw.entity.Task;
import com.example.one_mw.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepo;
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createTaskDefinition(Task task) throws Exception{
		taskRepo.save(task);
	}
}
