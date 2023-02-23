package com.example.one_mw.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRunRepository extends JpaRepository<com.example.one_mw.entity.ProcessRun, BigInteger> {

}
