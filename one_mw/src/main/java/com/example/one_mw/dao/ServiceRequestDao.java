package com.example.one_mw.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceRequest;

@Repository
public class ServiceRequestDao {

	@PersistenceContext
    private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<ServiceRequest> findPendingApprovalRequests(String accountId,List<String> privileges){
		
		
		List<Object[]> requests = entityManager.createNativeQuery(
			    "SELECT * FROM service_request_header AS SRH WHERE SRH.ACCOUNT_ID =:accountId "+
				"AND SRH.NEXT_CHECKER IN (:privilegeList) ORDER BY SRH.ID")
				.setParameter("accountId", accountId)
				.setParameter("privilegeList",privileges)
				.getResultList();
		/*
		List<Object[]> requests = entityManager.createNativeQuery(
			    "SELECT * FROM service_request_header AS SRH WHERE SRH.ACCOUNT_ID =:accountId "+
				"ORDER BY SRH.ID")
				.setParameter("accountId", accountId)
				.getResultList();*/
		
		List<ServiceRequest> result = new ArrayList<>();
		
		for(Object[] request : requests) {
			
			ServiceRequest serviceRequest = new ServiceRequest();
			
			serviceRequest.setId((BigInteger) request[0]);
			serviceRequest.setServiceName((String) request[2]);
			serviceRequest.setServiceRequestedBy((String) request[4]);
			serviceRequest.setStatus((String) request[11]);
			serviceRequest.setCreatedOn((Timestamp) request[12]);
			
		   
		    
		    result.add(serviceRequest);
		    
		}
		
		return result;

	}
}
