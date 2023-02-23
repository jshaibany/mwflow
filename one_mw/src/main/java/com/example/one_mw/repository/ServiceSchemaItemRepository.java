package com.example.one_mw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.one_mw.entity.ServiceSchemaItem;

@Repository
public interface ServiceSchemaItemRepository extends JpaRepository<ServiceSchemaItem, Integer> {

	public ServiceSchemaItem findByServiceId(Integer id);
}
