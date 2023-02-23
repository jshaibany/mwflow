package com.example.one_mw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceBulkTemp;

@Repository
public interface ServiceBulkTempRepository extends JpaRepository<ServiceBulkTemp, Integer>{

}
