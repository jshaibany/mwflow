package com.example.one_mw.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceBulkRequestChecker;


@Repository
public interface ServiceBulkRequestCheckerRepository extends JpaRepository<ServiceBulkRequestChecker, BigInteger> {

	public List<ServiceBulkRequestChecker> findByRequestId(BigInteger id);
}
