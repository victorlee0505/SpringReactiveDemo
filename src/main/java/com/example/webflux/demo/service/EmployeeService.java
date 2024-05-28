package com.example.webflux.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.example.webflux.demo.controller.model.Employee;
import com.example.webflux.demo.controller.model.EmployeeAddRequest;
import com.example.webflux.demo.controller.model.EmployeeApiResponse;
import com.example.webflux.demo.controller.model.EmployeeFull;
import com.example.webflux.demo.controller.model.EmployeeStatusType;
import com.example.webflux.demo.controller.model.EmployeeUpdateRequest;
import com.example.webflux.demo.database.EmployeeRepository;
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
    private final EmployeeMapper employeeMapper;

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
                .map(employeeMapper::mapEntityToEmployeeResponse);
    }

    public Mono<EmployeeApiResponse> updateEmployee(Long id, Mono<EmployeeUpdateRequest> employee) {
        log.info("Updating employee with id: {}", id);

        return employeeRepository.findById(id)
                .flatMap(existingEmployee -> employee
                        .map( request -> {
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
                        .flatMap(employeeRepository::save)
                        .map(employeeMapper::mapEntityToEmployeeResponse))
                .switchIfEmpty(Mono.just(EmployeeApiResponse.builder().message("No employee found with ID: " + id).build()));
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
}
