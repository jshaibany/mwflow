package com.example.one_mw.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.one_mw.entity.TaskSchemaItem;
import com.example.one_mw.repository.TaskSchemaItemRepository;

@Service
public class TaskSchemaItemService {

	@Autowired
	private TaskSchemaItemRepository taskSchemaItemRepo;
	
	@Transactional(rollbackFor = { SQLException.class })
	public void addTaskSchemaItem(TaskSchemaItem item) throws Exception{
		taskSchemaItemRepo.save(item);
	}
}
