package com.example.one_mw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends JpaRepository<com.example.one_mw.entity.Process, Integer> {

	com.example.one_mw.entity.Process findByName(String name);
}
