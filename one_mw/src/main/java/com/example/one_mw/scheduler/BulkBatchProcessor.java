package com.example.one_mw.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.one_mw.entity.ServiceBulkProcessingSchedule;
import com.example.one_mw.service.BatchProcessor;


@Service
public class BulkBatchProcessor {

	Logger logger = LogManager.getLogger(BulkBatchProcessor.class);
	
	private final BatchProcessor processor;

	@Autowired
	public BulkBatchProcessor(BatchProcessor processor) {
		super();
		this.processor = processor;
	}

	@Async
    @Scheduled(fixedRate = 1800000)
    public void scheduleBatchProcessingTaskAsync() throws InterruptedException {
        /*
         * Runs every 30 sec
         */
		
		logger.info(String.format("Scheduled Batch Processing started [%s] ..", LocalDateTime.now().toString()));
		
		List<ServiceBulkProcessingSchedule> schedules = processor.lockBulkSchedules();
		
		if(schedules != null)
		processor.processSchedules(schedules);
		
    }
	
}
