package com.example.webflux.demo.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import com.example.webflux.demo.controller.model.EmployeeStatusType;
import com.example.webflux.demo.database.entity.EmployeeEntity;

@DataR2dbcTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Test
    public void testChangeEmployeeStatusNotFound() {

        long id = 1;
        EmployeeEntity entity = employeeRepository.findById(id).block();

        entity.setStatus(EmployeeStatusType.INACTIVE.name());

        EmployeeEntity updated = employeeRepository.save(entity).block();

        assertEquals(EmployeeStatusType.INACTIVE.name(), updated.getStatus());
    }
}
