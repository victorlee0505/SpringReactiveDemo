package com.example.webflux.demo.service.calculator.function;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.example.webflux.demo.controller.model.CalculationRequest;
import com.example.webflux.demo.controller.model.CalculationResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InvalidOperandFunction implements Function<CalculationRequest, CalculationResponse> {

    Function<CalculationRequest, CalculationResponse> func = (request) -> {
        log.info("InvalidOperandFunction called");
        throw new IllegalArgumentException("Invalid operand: " + request.getOperand1() + " " + request.getOperator() + " " + request.getOperand2());
    };
    @Override
    public CalculationResponse apply(CalculationRequest t) {
        return this.func.apply(t);
    }
    
}
