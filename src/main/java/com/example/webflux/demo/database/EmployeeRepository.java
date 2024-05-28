package com.example.webflux.demo.database;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.demo.database.entity.EmployeeEntity;

import reactor.core.publisher.Flux;

public interface EmployeeRepository extends ReactiveCrudRepository<EmployeeEntity, Long> {
    Flux<EmployeeEntity> findByServiceYearsLessThan(int serviceYears);
}
