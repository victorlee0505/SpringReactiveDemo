package com.example.webflux.demo.service.calculator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.webflux.demo.controller.model.CalculationRequest;

public class OperatorPredicateTest {
    
    @Test
    public void testAdd() {
        CalculationRequest request = new CalculationRequest();
        request.setOperator("+");
        boolean result = OperatorPredicate.ADD.test(request);
        assertTrue(result);
    }

    @Test
    public void testPredicateMap() {
        CalculationRequest request = new CalculationRequest();
        request.setOperator("+");
        boolean result = OperatorPredicate.OPERATOR_MAP.get(OperatorEnum.ADD).test(request);
        assertTrue(result);
    }
}
