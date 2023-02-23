package com.example.one_mw.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceBulkRequest;


@Repository
public interface ServiceBulkRequestRepository extends JpaRepository<ServiceBulkRequest,BigInteger> {

	public List<ServiceBulkRequest> findByServiceId(Integer id);
	public List<ServiceBulkRequest> findByServiceIdAndServiceRequestMakerAndRequestAccountId(Integer id,String maker,String account);
}
