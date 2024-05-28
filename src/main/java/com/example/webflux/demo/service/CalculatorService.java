package com.example.webflux.demo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.webflux.demo.controller.model.CalculationRequest;

@Service
public class CalculatorService {
    
    public BigDecimal calculateResult(CalculationRequest calculationRequest) {
        BigDecimal result = new BigDecimal(0);

        switch (calculationRequest.getOperator()) {
            case "+":
                result = calculationRequest.getOperand1().add(calculationRequest.getOperand2());
                break;
            case "-":
                result = calculationRequest.getOperand1().subtract(calculationRequest.getOperand2());
                break;
            case "*":
                result = calculationRequest.getOperand1().multiply(calculationRequest.getOperand2());
                break;
            case "/":
                result = calculationRequest.getOperand1().divide(calculationRequest.getOperand2());
                break;
            default:
                // Invalid operator
                throw new IllegalArgumentException("Invalid operator: " + calculationRequest.getOperator());
        }
        
        return result;
    }
}
