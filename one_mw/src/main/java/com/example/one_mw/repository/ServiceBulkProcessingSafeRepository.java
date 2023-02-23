package com.example.one_mw.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceBulkProcessingSafe;

@Repository
public interface ServiceBulkProcessingSafeRepository extends JpaRepository<ServiceBulkProcessingSafe, BigInteger>{

	public List<ServiceBulkProcessingSafe> findByScheduleId(BigInteger scheduleId);
}
