package com.example.one_mw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service,Integer> {

	public Service findByCode(String code);
}
