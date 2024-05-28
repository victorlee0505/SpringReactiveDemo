package com.example.webflux.demo.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.MappingStyle;
import org.junit.jupiter.api.Test;

import com.example.webflux.demo.controller.model.AuditItem;
import com.example.webflux.demo.controller.model.EmployeePositionType;
import com.example.webflux.demo.controller.model.EmployeeStatusType;
import com.example.webflux.demo.database.entity.EmployeeAuditEntity;

import reactor.core.publisher.Flux;

public class EmployeeEntityDiffUtilTest {

    Javers javers = JaversBuilder.javers().withMappingStyle(MappingStyle.BEAN).build();
    EmployeeMapper employeeMapper = new EmployeeMapperImpl();
    EmployeeEntityDiffUtil employeeEntityDiffUtil = new EmployeeEntityDiffUtil(javers, employeeMapper);

    @Test
    public void testCompareEmployeeAuditEntity() {

        List<EmployeeAuditEntity> listEntity = createListOfEmployeeAuditEntities();

        List<AuditItem> listAudit = employeeEntityDiffUtil.compareEmployeeAuditEntity(listEntity.get(2), listEntity.get(1));

        assertTrue(listAudit.size() == 3);
        System.out.println(listAudit);
    }

    @Test
    public void testCompareConsecutiveEmployeeAuditEntityFlux() {

        Flux<EmployeeAuditEntity> fluxEntity = Flux.fromIterable(createListOfEmployeeAuditEntities());

        Flux<AuditItem> fluxAudit = employeeEntityDiffUtil.compareConsecutiveEmployees(fluxEntity);

        List<AuditItem> listAudit = fluxAudit.collectList().block();
        assertTrue(listAudit.size() == 5);
        System.out.println(listAudit);
    }

    @Test
    public void testCompareConsecutiveEmployeeAuditEntityList() {

        List<AuditItem> listAudit = employeeEntityDiffUtil.compareConsecutiveEmployees(createListOfEmployeeAuditEntities());

        assertTrue(listAudit.size() == 5);
        System.out.println(listAudit);
        }

    private List<EmployeeAuditEntity> createListOfEmployeeAuditEntities() {
        EmployeeAuditEntity entity1 = EmployeeAuditEntity.builder()
                .auditId(1L)
                .employeeId(1L)
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
                .operationUsername("admin")
                .operationType("UPDATE")
                .operationTimestamp(LocalDateTime.now().minusYears(20).minusMonths(3))
                .build();

        EmployeeAuditEntity entity2 = EmployeeAuditEntity.builder()
                .employeeId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test@test.com")
                .dateOfBirth(LocalDate.of(1992, 1, 1)) //5
                .position(EmployeePositionType.MANAGER.name()) //4
                .salary(BigDecimal.valueOf(30000)) //3
                .serviceYears(5)
                .address("123 Test St")
                .sinNumber("123-456-789")
                .driverLicenceNumber("123456789")
                .status(EmployeeStatusType.ACTIVE.name())
                .operationUsername("admin")
                .operationType("UPDATE")
                .operationTimestamp(LocalDateTime.now().minusYears(10).minusMonths(1))
                .build();

        EmployeeAuditEntity entity3 = EmployeeAuditEntity.builder()
                .employeeId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test@test.com")
                .dateOfBirth(LocalDate.of(1992, 1, 1))
                .position(EmployeePositionType.RETIRED.name()) //2
                .salary(BigDecimal.valueOf(30000))
                .serviceYears(5)
                .address("123 Test St")
                .sinNumber("123-456-789")
                .driverLicenceNumber("123456789")
                .status(EmployeeStatusType.INACTIVE.name()) //1
                .operationUsername("admin")
                .operationType("UPDATE")
                .operationTimestamp(LocalDate.now().atStartOfDay())
                .build();

        return List.of(entity3, entity2, entity1);
    }
}
