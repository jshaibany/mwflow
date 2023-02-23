package com.example.one_mw.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceBulkProcessingSchedule;

@Repository
public class ServiceBulkProcessingScheduleDao {

	@PersistenceContext
    private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<ServiceBulkProcessingSchedule> findPendingBulkSchedules(){
		
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		
		List<Object[]> checkers = entityManager.createNativeQuery(
			    "SELECT * FROM service_bulk_processing_schedule AS BPS WHERE BPS.SCHEDULED_ON <= :timestamp "+
				"AND BPS.STATUS = 'P' ORDER BY BPS.ID ASC")
				.setParameter("timestamp", timestamp)
				.getResultList();
		
		List<ServiceBulkProcessingSchedule> result = new ArrayList<>();
		
		for(Object[] checker : checkers) {
			
			ServiceBulkProcessingSchedule sch = new ServiceBulkProcessingSchedule();
			
			sch.setId((BigInteger) checker[0]);
			sch.setHeaderId((BigInteger) checker[1]);
			sch.setScheduledBy((String) checker[2]);
			sch.setScheduledOn((Timestamp) checker[3]);
			sch.setStatus((String) checker[4]);
			sch.setCreatedOn((Timestamp) checker[7]);
			
		    result.add(sch);
		    
		}
		
	return result;
	}
}
