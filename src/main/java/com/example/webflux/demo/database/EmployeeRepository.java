package com.example.webflux.demo.database;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.demo.database.entity.EmployeeEntity;

public interface EmployeeRepository extends ReactiveCrudRepository<EmployeeEntity, Long>{

}
