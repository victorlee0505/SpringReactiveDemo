package com.example.webflux.demo.controller;

import java.math.BigDecimal;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.example.webflux.demo.controller.model.Employee;
import com.example.webflux.demo.controller.model.EmployeeAddRequest;
import com.example.webflux.demo.controller.model.EmployeeApiResponse;
import com.example.webflux.demo.controller.model.EmployeeAuditResponse;
import com.example.webflux.demo.controller.model.EmployeeFull;
import com.example.webflux.demo.controller.model.EmployeePositionType;
import com.example.webflux.demo.controller.model.EmployeeStatusType;
import com.example.webflux.demo.controller.model.EmployeeUpdateRequest;
import com.example.webflux.demo.controller.model.PageEmployee;
import com.example.webflux.demo.controller.model.SortDirection;
import com.example.webflux.demo.controller.model.SortOptions;
import com.example.webflux.demo.service.EmployeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmployeeController implements EmployeesApi {

    private final EmployeeService employeeService;

    @Override
    public Mono<ResponseEntity<Flux<Employee>>> getAllEmployees(ServerWebExchange exchange) {
        log.info("Getting all employees");
        return Mono.just(ResponseEntity.ok(employeeService.getAllEmployees()));
    }

    @Override
    public Mono<ResponseEntity<Employee>> getEmployeeById(String id, ServerWebExchange exchange) {
        log.info("Getting employee by id: {}", id);
        return employeeService.getEmployeeById(Long.valueOf(id)).map(employee -> {
            if(employee.getId() != null) {
                return ResponseEntity.ok(employee);
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    @Override
    public Mono<ResponseEntity<EmployeeFull>> getEmployeeFullById(String id, ServerWebExchange exchange) {
        log.info("Getting employee full info by id: {}", id);
        return employeeService.getEmployeeFullById(Long.valueOf(id)).map(employee -> {
            if (employee.getId() != null) {
                return ResponseEntity.ok(employee);
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    @Override
    public Mono<ResponseEntity<Flux<Employee>>> getEmployeesByServiceYearsLessThan(String serviceYears,
            ServerWebExchange exchange) {
        log.info("Getting employees by service years less than: {}", serviceYears);
        return Mono.just(ResponseEntity.ok(employeeService.getEmployeesByServiceYearsLessThan(Integer.valueOf(serviceYears))));
    }

    @Override
    public Mono<ResponseEntity<EmployeeApiResponse>> addEmployee(@Valid Mono<EmployeeAddRequest> employeeAddRequest,
            ServerWebExchange exchange) {
        log.info("Adding employee: {}", employeeAddRequest);
        return employeeService.addEmployee(employeeAddRequest).map(response -> {
            if(response.getStatus().equals(true)) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        });
    }

    @Override
    public Mono<ResponseEntity<EmployeeApiResponse>> updateEmployee(String id,
            @Valid Mono<EmployeeUpdateRequest> employeeUpdateRequest, ServerWebExchange exchange) {
        log.info("Updating employee with id: {}", id);
        return employeeService.updateEmployee(Long.valueOf(id), employeeUpdateRequest).map(response -> {
            if (response.getStatus().equals(true)) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        });
    }

    @Override
    public Mono<ResponseEntity<Employee>> changeEmployeeStatus(String id, @NotNull @Valid EmployeeStatusType status,
            ServerWebExchange exchange) {
        log.info("change employee status with id: {}", id);
        return employeeService.changeEmployeeStatus(Long.valueOf(id), status).map(employee -> {
            if (employee.getId() != null) {
                return ResponseEntity.ok(employee);
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    @Override
    public Mono<ResponseEntity<EmployeeAuditResponse>> getEmployeeAuditById(String id, ServerWebExchange exchange) {
        log.info("Getting employee audit by id: {}", id);
        return employeeService.getEmployeeAuditById(id).map(response -> {
            if(response.getStatus().equals(true)) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        });
    }

    @Override
    public Mono<ResponseEntity<PageEmployee>> searchEmployees(@Valid String searchTerm,
            @Valid SortDirection dateOfBirth, @Valid SortDirection salary, @Valid SortDirection serviceYears,
            @Valid EmployeePositionType position, @Valid EmployeeStatusType status, @Valid BigDecimal salaryMin,
            @Valid BigDecimal salaryMax, @Valid Integer serviceYearsMin, @Valid Integer serviceYearsMax,
            @Valid Integer pageIndex, @Valid Integer pageSize, ServerWebExchange exchange) {

        log.info("Searching employees with search term: {}, dateOfBirth: {}, salary: {}, serviceYears: {}, position: {}, status: {}, salaryMin: {}, salaryMax: {}, serviceYearsMin: {}, serviceYearsMax: {}, pageIndex: {}, pageSize: {}",
                searchTerm, dateOfBirth, salary, serviceYears, position, status, salaryMin, salaryMax, serviceYearsMin,
                serviceYearsMax, pageIndex, pageSize);

        return employeeService
                .searchEmployees(searchTerm, dateOfBirth, salary, serviceYears, position, status, salaryMin, salaryMax,
                        serviceYearsMin, serviceYearsMax, pageIndex, pageSize)
                .map(ResponseEntity::ok);
    }
}
