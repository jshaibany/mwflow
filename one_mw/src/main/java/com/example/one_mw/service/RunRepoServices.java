package com.example.one_mw.service;

import java.sql.SQLException;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.one_mw.entity.ProcessRun;
import com.example.one_mw.entity.TaskRun;
import com.example.one_mw.repository.ProcessRunRepository;
import com.example.one_mw.repository.TaskRunRepository;


@Service
public class RunRepoServices {

	@Autowired
	private ProcessRunRepository processRunRepository;
	@Autowired
	private TaskRunRepository taskRunRepository;
	
	@Transactional(rollbackFor = { SQLException.class })
	public ProcessRun createNewProcessRun(ProcessRun process) {
		
		return processRunRepository.save(process);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public ProcessRun updateProcessRun(ProcessRun process) {
		
		return processRunRepository.save(process);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public TaskRun createNewTaskRun(TaskRun task) {
		
		return taskRunRepository.save(task);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public TaskRun updateTaskRun(TaskRun task) {
		
		return taskRunRepository.save(task);
	}
	
}
