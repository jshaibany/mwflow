package com.example.one_mw.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceBulkRequestLine;


@Repository
public interface ServiceBulkRequestLineRepository extends JpaRepository<ServiceBulkRequestLine,BigInteger>{

	List<ServiceBulkRequestLine> findByRequestId(BigInteger id);
	List<ServiceBulkRequestLine> findByRequestIdAndLineNumber(BigInteger id,Integer number);
}
