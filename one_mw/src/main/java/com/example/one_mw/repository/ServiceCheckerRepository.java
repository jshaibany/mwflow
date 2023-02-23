package com.example.one_mw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceChecker;

@Repository
public interface ServiceCheckerRepository extends JpaRepository<ServiceChecker,Integer> {

	public ServiceChecker findByServiceId(Integer id);
}
