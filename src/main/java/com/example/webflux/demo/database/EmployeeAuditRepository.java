package com.example.webflux.demo.database;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.demo.database.entity.EmployeeAuditEntity;

public interface EmployeeAuditRepository extends ReactiveCrudRepository<EmployeeAuditEntity, Long>{
    
}
