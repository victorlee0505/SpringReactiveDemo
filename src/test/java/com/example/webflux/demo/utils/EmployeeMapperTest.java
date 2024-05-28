package com.example.webflux.demo.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.webflux.demo.controller.model.Employee;
import com.example.webflux.demo.controller.model.EmployeeAddRequest;
import com.example.webflux.demo.controller.model.EmployeeApiResponse;
import com.example.webflux.demo.controller.model.EmployeeFull;
import com.example.webflux.demo.controller.model.EmployeePositionType;
import com.example.webflux.demo.controller.model.EmployeeStatusType;
import com.example.webflux.demo.database.entity.EmployeeAuditEntity;
import com.example.webflux.demo.database.entity.EmployeeEntity;

@SpringBootTest
public class EmployeeMapperTest {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    public void testMapToEmployee() {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test@test.com")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .position(EmployeePositionType.ADMIN.name())
                .salary(BigDecimal.valueOf(10000))
                .serviceYears(5)
                .address("123 Test St")
                .sinNumber("123-456-789")
                .driverLicenceNumber("123456789")
                .status(EmployeeStatusType.ACTIVE.name())
                .build();

        Employee employee = employeeMapper.mapToEmployee(employeeEntity);

        assertEquals(employeeEntity.getFirstName(), employee.getFirstName());
        assertEquals(employeeEntity.getLastName(), employee.getLastName());
        assertEquals(employeeEntity.getEmail(), employee.getEmail());
        assertEquals(employeeEntity.getDateOfBirth(), employee.getDateOfBirth());
        assertEquals(employeeEntity.getPosition(), employee.getPosition());
        assertEquals(employeeEntity.getSalary().doubleValue(), employee.getSalary());
        assertEquals(employeeEntity.getServiceYears(), employee.getServiceYears());

        EmployeeFull employeeFull = employeeMapper.mapToEmployeeFull(employeeEntity);

        assertEquals(employeeEntity.getFirstName(), employeeFull.getFirstName());
        assertEquals(employeeEntity.getLastName(), employeeFull.getLastName());
        assertEquals(employeeEntity.getEmail(), employeeFull.getEmail());
        assertEquals(employeeEntity.getDateOfBirth(), employeeFull.getDateOfBirth());
        assertEquals(employeeEntity.getPosition(), employeeFull.getPosition());
        assertEquals(employeeEntity.getSalary().doubleValue(), employeeFull.getSalary());
        assertEquals(employeeEntity.getServiceYears(), employeeFull.getServiceYears());
        assertEquals(employeeEntity.getAddress(), employeeFull.getAddress());
        assertEquals(employeeEntity.getSinNumber(), employeeFull.getSinNumber());
        assertEquals(employeeEntity.getDriverLicenceNumber(), employeeFull.getDriverLicenceNumber());
        assertEquals(employeeEntity.getStatus(), employeeFull.getStatus());
    }

    @Test
    public void testMapRequestToEmployeeEntity() {
        EmployeeAddRequest employeeAddRequest = EmployeeAddRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("test@test.com")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .position(EmployeePositionType.ADMIN.name())
                .salary(Float.valueOf(10000.00f))
                .serviceYears(5)
                .address("123 Test St")
                .sinNumber("123-456-789")
                .driverLicenceNumber("123456789")
                .status(EmployeeStatusType.ACTIVE.name())
                .build();

        EmployeeEntity employeeEntity = employeeMapper.mapRequestToEmployeeEntity(employeeAddRequest);

        assertEquals(employeeAddRequest.getFirstName(), employeeEntity.getFirstName());
        assertEquals(employeeAddRequest.getLastName(), employeeEntity.getLastName());
        assertEquals(employeeAddRequest.getEmail(), employeeEntity.getEmail());
        assertEquals(employeeAddRequest.getDateOfBirth(), employeeEntity.getDateOfBirth());
        assertEquals(employeeAddRequest.getPosition(), employeeEntity.getPosition());
        assertEquals(BigDecimal.valueOf(employeeAddRequest.getSalary()), employeeEntity.getSalary());
        assertEquals(employeeAddRequest.getServiceYears(), employeeEntity.getServiceYears());
        assertEquals(employeeAddRequest.getAddress(), employeeEntity.getAddress());
        assertEquals(employeeAddRequest.getSinNumber(), employeeEntity.getSinNumber());
        assertEquals(employeeAddRequest.getDriverLicenceNumber(), employeeEntity.getDriverLicenceNumber());
        assertEquals(employeeAddRequest.getStatus(), employeeEntity.getStatus());

    }

    @Test
    public void testMapEntityToEmployeeResponse() {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test@test.com")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .position(EmployeePositionType.ADMIN.name())
                .salary(BigDecimal.valueOf(10000))
                .serviceYears(5)
                .address("123 Test St")
                .sinNumber("123-456-789")
                .driverLicenceNumber("123456789")
                .status(EmployeeStatusType.ACTIVE.name())
                .build();

        EmployeeApiResponse employeeAddResponse = employeeMapper.mapEntityToEmployeeResponse(employeeEntity);

        assertEquals(employeeEntity.getId(), employeeAddResponse.getEmployee().getId());
        assertEquals(employeeEntity.getFirstName(), employeeAddResponse.getEmployee().getFirstName());
        assertEquals(employeeEntity.getLastName(), employeeAddResponse.getEmployee().getLastName());
        assertEquals(employeeEntity.getEmail(), employeeAddResponse.getEmployee().getEmail());
        assertEquals(employeeEntity.getDateOfBirth(), employeeAddResponse.getEmployee().getDateOfBirth());
        assertEquals(employeeEntity.getPosition(), employeeAddResponse.getEmployee().getPosition());
        assertEquals(employeeEntity.getSalary().doubleValue(), employeeAddResponse.getEmployee().getSalary());
        assertEquals(employeeEntity.getServiceYears(), employeeAddResponse.getEmployee().getServiceYears());
        assertEquals(employeeEntity.getAddress(), employeeAddResponse.getEmployee().getAddress());
        assertEquals(employeeEntity.getSinNumber(), employeeAddResponse.getEmployee().getSinNumber());
        assertEquals(employeeEntity.getDriverLicenceNumber(), employeeAddResponse.getEmployee().getDriverLicenceNumber());
        assertEquals(employeeEntity.getStatus(), employeeAddResponse.getEmployee().getStatus());
        
    }

    @Test
    public void testMapEntityToEmployeeAudit() {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test@test.com")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .position(EmployeePositionType.ADMIN.name())
                .salary(BigDecimal.valueOf(10000))
                .serviceYears(5)
                .address("123 Test St")
                .sinNumber("123-456-789")
                .driverLicenceNumber("123456789")
                .status(EmployeeStatusType.ACTIVE.name())
                .build();

        EmployeeAuditEntity employeeAuditEntity = employeeMapper.mapEntityToEmployeeAudit(employeeEntity);

        assertEquals(employeeEntity.getId(), employeeAuditEntity.getEmployeeId());
        assertEquals(employeeEntity.getFirstName(), employeeAuditEntity.getFirstName());
        assertEquals(employeeEntity.getLastName(), employeeAuditEntity.getLastName());
        assertEquals(employeeEntity.getEmail(), employeeAuditEntity.getEmail());
        assertEquals(employeeEntity.getDateOfBirth(), employeeAuditEntity.getDateOfBirth());
        assertEquals(employeeEntity.getPosition(), employeeAuditEntity.getPosition());
        assertEquals(employeeEntity.getSalary().doubleValue(), employeeAuditEntity.getSalary().doubleValue());
        assertEquals(employeeEntity.getServiceYears(), employeeAuditEntity.getServiceYears());
        assertEquals(employeeEntity.getAddress(), employeeAuditEntity.getAddress());
        assertEquals(employeeEntity.getSinNumber(), employeeAuditEntity.getSinNumber());
        assertEquals(employeeEntity.getDriverLicenceNumber(), employeeAuditEntity.getDriverLicenceNumber());
        assertEquals(employeeEntity.getStatus(), employeeAuditEntity.getStatus());

        assertNull(employeeAuditEntity.getAuditId());
        assertNull(employeeAuditEntity.getOperationUsername());
        assertNull(employeeAuditEntity.getOperationType());
        assertNull(employeeAuditEntity.getOperationTimestamp());

    }
}
