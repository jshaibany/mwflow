package com.example.one_mw.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceRequestChecker;


@Repository
public interface ServiceRequestCheckerRepository extends JpaRepository<ServiceRequestChecker, BigInteger> {

	public List<ServiceRequestChecker> findByRequestId(BigInteger id);
}
