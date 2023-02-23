package com.example.one_mw.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceBulkRequestChecker;


@Repository
public class ServiceBulkRequestCheckerDao {

	@PersistenceContext
    private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<ServiceBulkRequestChecker> findNextCheckers(BigInteger requestId){
		
		List<Object[]> checkers = entityManager.createNativeQuery(
			    "SELECT * FROM service_bulk_request_checker AS SRC WHERE SRC.REQUEST_HEADER_ID =:requestId "+
				"AND SRC.IS_CURRENT_CHECKER IS NULL ORDER BY SRC.ID ASC LIMIT 1")
				.setParameter("requestId", requestId)
				.getResultList();
		
		List<ServiceBulkRequestChecker> result = new ArrayList<>();
		
		for(Object[] checker : checkers) {
			
			ServiceBulkRequestChecker chkr = new ServiceBulkRequestChecker();
			
			chkr.setId((BigInteger) checker[0]);
			chkr.setRequestId((BigInteger) checker[1]);
			chkr.setRequestCheckerPrivilege((String) checker[2]);
			chkr.setCreatedOn((Timestamp) checker[7]);
		    
		    result.add(chkr);
		    
		}
		
	return result;
	}
}
