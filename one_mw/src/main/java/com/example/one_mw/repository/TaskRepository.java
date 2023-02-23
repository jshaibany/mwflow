package com.example.one_mw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<com.example.one_mw.entity.Task,Integer>{

}
