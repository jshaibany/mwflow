package com.example.one_mw.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.one_mw.entity.ServiceBulkProcessingReport;

public interface ServiceBulkProcessingReportRepository extends JpaRepository<ServiceBulkProcessingReport, BigInteger>{

	public List<ServiceBulkProcessingReport> findByHeaderId(BigInteger id);
}
