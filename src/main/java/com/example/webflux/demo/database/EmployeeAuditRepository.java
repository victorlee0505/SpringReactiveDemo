package com.example.webflux.demo.database;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.demo.database.entity.EmployeeAuditEntity;

import reactor.core.publisher.Flux;

public interface EmployeeAuditRepository extends ReactiveCrudRepository<EmployeeAuditEntity, Long>{
    Flux<EmployeeAuditEntity> findAllByEmployeeIdOrderByOperationTimestampDesc(Long employeeId);
}
