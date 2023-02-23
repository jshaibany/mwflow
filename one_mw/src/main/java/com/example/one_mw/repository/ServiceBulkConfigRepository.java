package com.example.one_mw.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceBulkConfig;


@Repository
public interface ServiceBulkConfigRepository extends JpaRepository<ServiceBulkConfig,Integer>{

	public List<ServiceBulkConfig> findByServiceId(Integer id);
}
