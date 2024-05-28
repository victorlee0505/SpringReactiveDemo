package com.example.webflux.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.webflux.demo.controller.model.AuditOperationType;
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
import com.example.webflux.demo.database.EmployeeAuditRepository;
import com.example.webflux.demo.database.EmployeeRepository;
import com.example.webflux.demo.database.entity.EmployeeAuditEntity;
import com.example.webflux.demo.database.entity.EmployeeEntity;
import com.example.webflux.demo.utils.EmployeeEntityDiffUtil;
import com.example.webflux.demo.utils.EmployeeMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeAuditRepository employeeAuditRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeEntityDiffUtil employeeEntityDiffUtil;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Flux<Employee> getAllEmployees() {
        log.info("Getting all employees");

        return employeeRepository.findAll()
                .map(employeeMapper::mapToEmployee);
    }

    public Mono<Employee> getEmployeeById(Long id) {
        log.info("Getting employee by id: {}", id);
        return employeeRepository.findById(id)
                .map(employeeMapper::mapToEmployee);
    }

    public Mono<EmployeeFull> getEmployeeFullById(Long id) {
        log.info("Getting full employee by id: {}", id);
        return employeeRepository.findById(id)
                .map(employeeMapper::mapToEmployeeFull);
    }

    public Flux<Employee> getEmployeesByServiceYearsLessThan(Integer serviceYears) {
        log.info("Getting employees by service years less than: {}", serviceYears);
        return employeeRepository.findByServiceYearsLessThan(serviceYears)
                .map(employeeMapper::mapToEmployee);
    }

    public Mono<EmployeeApiResponse> addEmployee(Mono<EmployeeAddRequest> employee) {
        log.info("Adding employee: {}", employee);

        return employee
                .map(employeeMapper::mapRequestToEmployeeEntity)
                .flatMap(employeeRepository::save)
                .flatMap(saved -> {
                    return employeeRepository.save(saved).flatMap(updated -> {
                        return createEmployeeAudit(updated, "admin", AuditOperationType.ADD.name());
                    });
                })
                .map(employeeMapper::mapEntityToEmployeeResponse);
    }

    public Mono<EmployeeApiResponse> updateEmployee(Long id, Mono<EmployeeUpdateRequest> employeeUpdateRequest) {
        log.info("Updating employee with id: {}", id);

        return employeeRepository.findById(id)
                .flatMap(existingEmployee -> {

                    EmployeeEntity originalEmployee = existingEmployee.toBuilder().build();

                    return employeeUpdateRequest.map(request -> {
                        existingEmployee.setFirstName(request.getEmployee().getFirstName());
                        existingEmployee.setLastName(request.getEmployee().getLastName());
                        existingEmployee.setEmail(request.getEmployee().getEmail());
                        existingEmployee.setDateOfBirth(request.getEmployee().getDateOfBirth());
                        existingEmployee.setPosition(request.getEmployee().getPosition());
                        existingEmployee.setSalary(BigDecimal.valueOf(request.getEmployee().getSalary()));
                        existingEmployee.setServiceYears(request.getEmployee().getServiceYears());
                        existingEmployee.setAddress(request.getEmployee().getAddress());
                        existingEmployee.setSinNumber(request.getEmployee().getSinNumber());
                        existingEmployee.setDriverLicenceNumber(request.getEmployee().getDriverLicenceNumber());
                        existingEmployee.setStatus(request.getEmployee().getStatus());
                        existingEmployee.setUpdateDatetime(LocalDateTime.now());
                        return existingEmployee;
                    })
                            .flatMap(toBeUpdated -> {
                                return createEmployeeAudit(originalEmployee, "admin", AuditOperationType.UPDATE.name())
                                        .then(employeeRepository.save(toBeUpdated));
                            })
                            .map(employeeMapper::mapEntityToEmployeeResponse);
                })
                .switchIfEmpty(
                        Mono.just(EmployeeApiResponse.builder().message("No employee found with ID: " + id).build()));
    }

    public Mono<Employee> changeEmployeeStatus(Long id, EmployeeStatusType status) {
        log.info("Deleting employee with id: {}", id);

        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setStatus(status.name());
                    return employee;
                })
                .flatMap(employeeRepository::save)
                .flatMap(updated -> Mono.just(employeeMapper.mapToEmployee(updated)))
                .switchIfEmpty(Mono.error(new NoSuchElementException("No employee found with ID: " + id)));
    }

    public Mono<EmployeeEntity> createEmployeeAudit(EmployeeEntity employee, String username, String operationType) {
        log.info("Creating audit for employee: {}", employee.getId());
        EmployeeAuditEntity auditEntity = createEmployeeAuditEntity(employee, username, operationType, LocalDateTime.now());
        return employeeAuditRepository.save(auditEntity)
                .thenReturn(employee);
    }

    public EmployeeAuditEntity createEmployeeAuditEntity(EmployeeEntity employee, String username, String operationType, LocalDateTime operationTimestamp) {
        log.info("Creating audit for employee: {}", employee.getId());
        EmployeeAuditEntity auditEntity = employeeMapper.mapEntityToEmployeeAudit(employee);

        auditEntity.setOperationUsername(username);
        auditEntity.setOperationType(operationType);
        auditEntity.setOperationTimestamp(operationTimestamp);

        return auditEntity;
    } 

    /**
     * FYI , the following code is the same with a different approach
     * 
     * <pre>{@code
     * 
     * public Mono<EmployeeAuditResponse> getEmployeeAuditById(String id) {
     *     log.info("Getting audit for employee: {}", id);
     *     Mono<EmployeeEntity> employeeMono = employeeRepository.findById(id);
     * 
     *     Mono<EmployeeAuditEntity> auditEntityMono = employeeMono.map(employee -> employeeMapper.mapEntityToEmployeeAudit(employee));
     * 
     *     Flux<EmployeeAuditEntity> auditFlux = employeeAuditRepository.findAllByEmployeeIdOrderByOperationTimestampDesc(Long.valueOf(id));
     * 
     *      auditEntityMono = auditEntityMono.fluc().concatWith(auditFlux);
     * 
     *     Flux<AuditItem> itemsFlux = employeeEntityDiffUtil.compareConsecutiveEmployees(auditFlux);
     * 
     *     return itemsFlux.collectList().map(auditItems -> {
     *         if (auditItems.isEmpty()) {
     *             return new EmployeeAuditResponse(false, "No audit entries found for employee with ID: " + id, null);
     *         } else {
     *             return new EmployeeAuditResponse(true, "Audit entries retrieved successfully.", auditItems);
     *         }
     *     }).onErrorResume(e -> {
     *         log.error("Error retrieving audit for employee: {}", id, e);
     *         return Mono.just(new EmployeeAuditResponse(false, "Error retrieving audit data", null));
     *     });
     * }
     * }</pre>
     * 
     * @param id
     * 
     * @return Mono<EmployeeAuditResponse>
     */
    public Mono<EmployeeAuditResponse> getEmployeeAuditById(String id) {
        log.info("Getting audit for employee: {}", id);

        return employeeRepository.findById(Long.valueOf(id))
                .map(employee -> createEmployeeAuditEntity(employee, "admin", AuditOperationType.UPDATE.name(),
                        employee.getUpdateDatetime()))
                .flux()
                .concatWith(employeeAuditRepository.findAllByEmployeeIdOrderByOperationTimestampDesc(Long.valueOf(id)))
                .transformDeferred(flux -> employeeEntityDiffUtil.compareConsecutiveEmployees(flux))
                .collectList()
                .map(auditItems -> {
                    if (auditItems.isEmpty()) {
                        return new EmployeeAuditResponse(false, "No audit entries found for employee with ID: " + id,
                                null);
                    } else {
                        return new EmployeeAuditResponse(true, "Audit entries retrieved successfully.", auditItems);
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error retrieving audit for employee: {}", id, e);
                    return Mono.just(new EmployeeAuditResponse(false, "Error retrieving audit data", null));
                });
    }

}
