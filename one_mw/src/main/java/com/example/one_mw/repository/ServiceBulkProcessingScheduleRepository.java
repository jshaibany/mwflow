package com.example.one_mw.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceBulkProcessingSchedule;

@Repository
public interface ServiceBulkProcessingScheduleRepository extends JpaRepository<ServiceBulkProcessingSchedule,BigInteger>{

	public ServiceBulkProcessingSchedule findByHeaderId(BigInteger id);
}
