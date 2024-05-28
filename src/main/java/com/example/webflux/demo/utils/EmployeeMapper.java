package com.example.webflux.demo.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.webflux.demo.controller.model.Employee;
import com.example.webflux.demo.controller.model.EmployeeAddRequest;
import com.example.webflux.demo.controller.model.EmployeeApiResponse;
import com.example.webflux.demo.controller.model.EmployeeAuditData;
import com.example.webflux.demo.controller.model.EmployeeFull;
import com.example.webflux.demo.database.entity.EmployeeAuditEntity;
import com.example.webflux.demo.database.entity.EmployeeEntity;

/**
 * EmployeeEntity has sinNumber and driverLicenceNumber fields that are not present in Employee
 * Employee object do not contain confidential information
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee mapToEmployee(EmployeeEntity employeeEntity);

    EmployeeFull mapToEmployeeFull(EmployeeEntity employeeEntity);

    // for saving to DB
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDatetime", ignore = true)
    @Mapping(target = "updateDatetime", ignore = true)
    EmployeeEntity mapRequestToEmployeeEntity(EmployeeAddRequest employee);

    // for DB to EmployeeAddREsponse
    @Mapping(target = "message", ignore = true)
    @Mapping(target = "status", ignore = true)
    EmployeeApiResponse mapEntityToEmployeeResponse(EmployeeEntity employee);

    // for record Audit to DB
    @Mapping(source = "id", target = "employeeId")
    @Mapping(target = "auditId", ignore = true)
    @Mapping(target = "operationUsername", ignore = true)
    @Mapping(target = "operationType", ignore = true)
    @Mapping(target = "operationTimestamp", ignore = true)
    EmployeeAuditEntity mapEntityToEmployeeAudit(EmployeeEntity employeeEntity);

    // for produce AuditItem
    EmployeeAuditData mapEntityToEmployeeAuditData(EmployeeAuditEntity employeeEntity);
}
