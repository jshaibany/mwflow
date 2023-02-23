package com.example.one_mw.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceRequest;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest,BigInteger> {

	public List<ServiceRequest> findByServiceId(Integer id);
	public List<ServiceRequest> findByServiceIdAndServiceRequestMakerAndRequestAccountId(Integer id,String maker,String account);
}
