package com.example.one_mw.controller;




import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.example.one_mw.entity.Task;

import com.example.one_mw.entity.TaskSchemaItem;


import com.example.one_mw.service.TaskSchemaItemService;
import com.example.one_mw.service.TaskService;


@RestController
public class TaskController {

	@Autowired
	private TaskService taskService;
	
	
	
	@Autowired
	private TaskSchemaItemService taskSchemaItemService;


	
	
	
	@GetMapping("/v1/createTaskDefinition")
	@ResponseBody
	public Task createTaskDefinition() {
		
		Task task = new Task();
		
		task.setName("Task2");
		task.setDescription("Test");
		task.setRequestBody("Body");
		
		try {
			taskService.createTaskDefinition(task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return task;
		
	}
	
	@GetMapping("/v1/createTaskSchemaItem")
	@ResponseBody
	public TaskSchemaItem createTaskSchemaItem() {
		
		TaskSchemaItem taskSchemaItem = new TaskSchemaItem();
		
		taskSchemaItem.setTaskId(2);
		taskSchemaItem.setName("Item1");
		taskSchemaItem.setDescription("Item1");
		taskSchemaItem.setDataType("String");
		taskSchemaItem.setIsNillable(false);
		taskSchemaItem.setIsPersistable(false);
		taskSchemaItem.setItemDirection("REQUEST");
		
		try {
			taskSchemaItemService.addTaskSchemaItem(taskSchemaItem);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return taskSchemaItem;
		
	}

	
}
