package com.example.webflux.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.example.webflux.demo.controller.model.Employee;
import com.example.webflux.demo.controller.model.EmployeeFull;
import com.example.webflux.demo.controller.model.EmployeeStatusType;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;
    
    @Test
    public void testChangeEmployeeStatus() {

        long id = 3L;
        EmployeeStatusType status = EmployeeStatusType.INACTIVE;

        EmployeeFull employee = employeeService.getEmployeeFullById(id).block();

        log.info(employee.toString()); 

        Employee updated = employeeService.changeEmployeeStatus(id, status).block();

        assertEquals(status.name(), updated.getStatus());

    }

}
