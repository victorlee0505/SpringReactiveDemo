package com.example.webflux.demo.service.calculator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.webflux.demo.controller.model.CalculationRequest;

public class CalculationRequestPredicateTest {

    @Test
    public void testInvalidaOperand1() {
        CalculationRequest request = new CalculationRequest();
        request.setOperator("+");
        // request.setOperand1(null);
        request.setOperand2(new BigDecimal(2));

        boolean result = CalculationRequestPredicate.IS_INVALID_OPERAND.test(request);
        assertTrue(result);
    }

    @Test
    public void testInvalidaOperand2() {
        CalculationRequest request = new CalculationRequest();
        request.setOperator("+");
        request.setOperand1(new BigDecimal(1));
        // request.setOperand2(null);

        boolean result = CalculationRequestPredicate.IS_INVALID_OPERAND.test(request);
        assertTrue(result);
    }
    
    @Test
    public void testInvalidaOperator() {
        CalculationRequest request = new CalculationRequest();
        request.setOperator("?");
        request.setOperand1(new BigDecimal(1));
        request.setOperand2(new BigDecimal(2));

        boolean result = CalculationRequestPredicate.IS_INVALID_OPERATOR.test(request);
        assertTrue(result);
    }

    @Test
    public void testInvalidaOperatorNull() {
        CalculationRequest request = new CalculationRequest();
        request.setOperator(null);
        request.setOperand1(new BigDecimal(1));
        request.setOperand2(new BigDecimal(2));

        boolean result = CalculationRequestPredicate.IS_INVALID_OPERATOR.test(request);
        assertTrue(result);
    }

    @Test
    public void testValidaOperatorEmpty() {
        CalculationRequest request = new CalculationRequest();
        request.setOperator("+");
        request.setOperand1(new BigDecimal(1));
        request.setOperand2(new BigDecimal(2));

        boolean result = CalculationRequestPredicate.IS_INVALID_OPERATOR.test(request);
        assertFalse(result);
    }
}
