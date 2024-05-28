package com.example.webflux.demo.database.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "EMPLOYEE_AUDIT")
public class EmployeeAuditEntity {
    @Id
    private Long auditId;

    private Long employeeId;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate dateOfBirth;

    private String position;

    private BigDecimal salary;

    private int serviceYears;

    private String address;

    private String sinNumber;

    private String driverLicenceNumber;

    private String status;

    private String operationUsername;

    private String operationType;

    private LocalDateTime operationTimestamp;
}
