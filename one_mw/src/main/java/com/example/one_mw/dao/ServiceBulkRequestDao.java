package com.example.one_mw.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceBulkRequest;

@Repository
public class ServiceBulkRequestDao {

	@PersistenceContext
    private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<ServiceBulkRequest> findPendingApprovalRequests(String accountId,List<String> privileges){
		
		
		List<Object[]> requests = entityManager.createNativeQuery(
			    "SELECT * FROM service_bulk_request_header AS SRH WHERE SRH.ACCOUNT_ID =:accountId "+
				"AND SRH.NEXT_CHECKER IN (:privilegeList) ORDER BY SRH.ID")
				.setParameter("accountId", accountId)
				.setParameter("privilegeList",privileges)
				.getResultList();
	
		
		List<ServiceBulkRequest> result = new ArrayList<>();
		
		for(Object[] request : requests) {
			
			ServiceBulkRequest serviceRequest = new ServiceBulkRequest();
			
			serviceRequest.setId((BigInteger) request[0]);
			serviceRequest.setServiceName((String) request[2]);
			serviceRequest.setServiceRequestedBy((String) request[4]);
			serviceRequest.setStatus((String) request[11]);
			serviceRequest.setCreatedOn((Timestamp) request[12]);
			
		   
		    
		    result.add(serviceRequest);
		    
		}
		
		return result;

	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getRequestLineNumbers(BigInteger requestId){
		
		
		List<Integer> requests = entityManager.createNativeQuery(
			    "SELECT DISTINCT LINE_NO FROM service_bulk_request_line AS SRL WHERE SRL.REQUEST_HEADER_ID =:requestId "+
				"ORDER BY SRL.LINE_NO ASC")
				.setParameter("requestId", requestId)
				.getResultList();
	
		/*
		List<Integer> result = new ArrayList<>();
		
		for(Object[] request : requests) {
 
		    result.add(Integer.valueOf(request[0].toString()));
		    
		}*/
		
		return requests;

	}
}
