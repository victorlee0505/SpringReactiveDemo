package com.example.webflux.demo.utils;

import java.util.ArrayList;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.stereotype.Component;

import com.example.webflux.demo.controller.model.AuditItem;
import com.example.webflux.demo.database.entity.EmployeeAuditEntity;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class EmployeeEntityDiffUtil {

    private final Javers javers;
    private final EmployeeMapper employeeMapper;

    public List<AuditItem> compareEmployeeAuditEntity(EmployeeAuditEntity existingProfile,
            EmployeeAuditEntity updatedProfile) {

        Diff diff = javers.compare(employeeMapper.mapEntityToEmployeeAuditData(existingProfile), employeeMapper.mapEntityToEmployeeAuditData(updatedProfile));

        List<ValueChange> valueChanges = diff.getChangesByType(ValueChange.class);

        List<AuditItem> auditItems = new ArrayList<>();

        for (ValueChange valueChange : valueChanges) {
            AuditItem auditItem = AuditItem.builder()
                    .field(valueChange.getPropertyName())
                    .oldValue(String.valueOf(valueChange.getLeft()))
                    .newValue(String.valueOf(valueChange.getRight()))
                    .username(updatedProfile.getOperationUsername())
                    .modifiedDate(updatedProfile.getOperationTimestamp().toLocalDate())
                    .build();
            auditItems.add(auditItem);
        }

        return auditItems;
    }

    public Flux<AuditItem> compareConsecutiveEmployees(Flux<EmployeeAuditEntity> employeeFlux) {
        return employeeFlux.buffer(2, 1)
                .filter(pair -> pair.size() == 2) // Ensure that there are always two elements to compare
                .flatMapIterable(pair -> compareEmployeeAuditEntity(pair.get(1), pair.get(0)));
    }

    public List<AuditItem> compareConsecutiveEmployees(List<EmployeeAuditEntity> employees) {
        List<AuditItem> auditItems = new ArrayList<>();

        for (int i = 1; i < employees.size(); i++) {
            EmployeeAuditEntity previous = employees.get(i - 1);
            EmployeeAuditEntity current = employees.get(i);

            compareEmployeeAuditEntity(previous, current).forEach(auditItems::add);

        }
        return auditItems;
    }

}
