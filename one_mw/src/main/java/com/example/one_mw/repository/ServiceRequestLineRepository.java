package com.example.one_mw.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceRequestLine;

@Repository
public interface ServiceRequestLineRepository extends JpaRepository<ServiceRequestLine,BigInteger>{

	List<ServiceRequestLine> findByRequestId(BigInteger id);
}
