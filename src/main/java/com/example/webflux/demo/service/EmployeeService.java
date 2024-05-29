package com.example.webflux.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with ID: " + id)))
                .map(employeeMapper::mapToEmployee)
                .onErrorResume(e -> {
                    log.error("Error getting employee by id: {}", id, e);
                    return Mono.just(Employee.builder().build());
                });
    }

    public Mono<EmployeeFull> getEmployeeFullById(Long id) {
        log.info("Getting full employee by id: {}", id);
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with ID: " + id)))
                .map(employeeMapper::mapToEmployeeFull)
                .onErrorResume(e -> {
                    log.error("Error getting employee by id: {}", id, e);
                    return Mono.just(EmployeeFull.builder().build());
                });
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
                .map(employeeMapper::mapEntityToEmployeeResponse)
                .map(response -> {
                    response.setStatus(true);
                    response.setMessage("Employee added successfully.");
                    return response;
                })
                .onErrorResume(e -> {
                    log.error("Error adding employee", e);
                    return Mono.just(EmployeeApiResponse.builder().status(false).message("Error adding employee").build());
                });
    }

    public Mono<EmployeeApiResponse> updateEmployee(Long id, Mono<EmployeeUpdateRequest> employeeUpdateRequest) {
        log.info("Updating employee with id: {}", id);

        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with ID: " + id)))
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
                        Mono.just(EmployeeApiResponse.builder().message("No employee found with ID: " + id).build()))
                .onErrorResume(e -> {
                    log.error("Error updating employee", e);
                    return Mono.just(EmployeeApiResponse.builder().status(false).message("Error updating employee: " + e.getMessage()).build());
                });
    }

    public Mono<Employee> changeEmployeeStatus(Long id, EmployeeStatusType status) {
        log.info("Deleting employee with id: {}", id);

        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with ID: " + id)))
                .map(employee -> {
                    employee.setStatus(status.name());
                    return employee;
                })
                .flatMap(employeeRepository::save)
                .flatMap(updated -> Mono.just(employeeMapper.mapToEmployee(updated)))
                .onErrorResume(e -> {
                    log.error("Error changing employee status", e);
                    return Mono.just(Employee.builder().build());
                });
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
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with ID: " + id)))
                .map(employee -> createEmployeeAuditEntity(employee, "admin", AuditOperationType.UPDATE.name(),
                        employee.getUpdateDatetime()))
                .flux()
                .concatWith(employeeAuditRepository.findAllByEmployeeIdOrderByOperationTimestampDesc(Long.valueOf(id)))
                .transformDeferred(flux -> employeeEntityDiffUtil.compareConsecutiveEmployees(flux))
                .collectList()
                .map(auditItems -> {
                    if (auditItems.isEmpty()) {
                        return new EmployeeAuditResponse(true, "No audit entries found for employee with ID: " + id,
                                null);
                    } else {
                        return new EmployeeAuditResponse(true, "Audit entries retrieved successfully.", auditItems);
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error retrieving audit for employee: {}", id, e);
                    return Mono.just(EmployeeAuditResponse.builder().status(false).message("Error retrieving audit data: " + e.getMessage()).build());
                });
    }

        /**
     * Search term will search in first name, last name, date of birth, email, address
     * Sort one column by either dateOfBirth, salary, or serviceYears
     * sortOrder is either ASC or DESC
     * filter by position and status
     * filter by salaryMin and salaryMax
     * filter by serviceYearsMin and serviceYearsMax
     * pagination by page and size
     * 
     * @param searchTerm
     * @param sort
     * @return
     */
    public Mono<PageEmployee> searchEmployees(String searchTerm, SortDirection dateOfBirth, SortDirection salary,
            SortDirection serviceYears, EmployeePositionType position, EmployeeStatusType status, BigDecimal salaryMin,
            BigDecimal salaryMax, Integer serviceYearsMin, Integer serviceYearsMax, Integer pageNumber,
            Integer pageSize) {

        Criteria criteria = Criteria.empty();

        if (StringUtils.isNotBlank(searchTerm)) {
            searchTerm = searchTerm.trim();
            criteria = Criteria.where("firstName").like("%" + searchTerm + "%")
                    .or("lastName").like("%" + searchTerm + "%")
                    .or("email").like("%" + searchTerm + "%")
                    // .or("dateOfBirth").like("%" + searchTerm + "%") // does not work
                    .or("address").like("%" + searchTerm + "%");
        }

        if (position != null) {
            criteria = criteria.and("position").is(position.name());
        }

        if (status != null) {
            criteria = criteria.and("status").is(status.name());
        }

        if (salaryMin != null) {
            criteria = criteria.and("salary").greaterThanOrEquals(salaryMin);
        }

        if (salaryMax != null) {
            criteria = criteria.and("salary").lessThanOrEquals(salaryMax);
        }

        if (serviceYearsMin != null) {
            criteria = criteria.and("serviceYears").greaterThanOrEquals(serviceYearsMin);
        }

        if (serviceYearsMax != null) {
            criteria = criteria.and("serviceYears").lessThanOrEquals(serviceYearsMax);
        }

        Map<String, SortDirection> sortMap = new HashMap<>();
        if (dateOfBirth != null) {
            sortMap.put("dateOfBirth", dateOfBirth);
        }
        if (salary != null) {
            sortMap.put("salary", salary);
        }
        if (serviceYears != null) {
            sortMap.put("serviceYears", serviceYears);
        }

        Sort sort = null;
        for (Map.Entry<String, SortDirection> entry : sortMap.entrySet()) {
            sort = Sort.by(Sort.Order.by(entry.getKey()));
            if (entry.getValue() != null) {
                if (entry.getValue().equals(SortDirection.ASC)) {
                    sort = sort.ascending();
                } else if (entry.getValue().equals(SortDirection.DESC)) {
                    sort = sort.descending();
                } else {
                    sort = Sort.unsorted();
                }
            }
        }

        if (sort == null) {
            sort = Sort.unsorted();
        }

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize == null || pageSize < 25) {
            pageSize = 25;
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Query query = Query.query(criteria).sort(sort).with(pageRequest);
        Mono<Long> countMono = r2dbcEntityTemplate.count(query, EmployeeEntity.class);
        Flux<EmployeeEntity> employeeFlux = r2dbcEntityTemplate.select(query, EmployeeEntity.class);

        return employeeFlux.collectList()
                .zipWith(countMono, (employees, count) -> new PageImpl<>(employees, pageRequest, count))
                .map(employeeMapper::mapPageEmployeeEntityToPageEmployee);
    }

}
