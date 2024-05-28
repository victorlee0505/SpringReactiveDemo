package com.example.webflux.demo.service.calculator;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.example.webflux.demo.controller.model.CalculationRequest;
import com.example.webflux.demo.controller.model.CalculationResponse;
import com.example.webflux.demo.service.calculator.function.AddFunction;
import com.example.webflux.demo.service.calculator.function.InvalidOperandFunction;
import com.example.webflux.demo.service.calculator.function.InvalidOperationFunction;
import com.example.webflux.demo.service.calculator.function.InvalidOperatorFunction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalculatorFunctionFactory {
    
    private final InvalidOperatorFunction invalidOperatorFunction;
    private final InvalidOperandFunction invalidOperandFunction;
    private final InvalidOperationFunction invalidOperationFunction;

    private final AddFunction addFunction;

    public Function<CalculationRequest, CalculationResponse> getFunction(CalculationRequest request) {
        
        String operation = CalculatorPredicateUtil.findOperation(request);

        switch (operation) {
            case InvalidConstant.INVALID_OPERATOR:
                return this.invalidOperatorFunction;
            case InvalidConstant.INVALID_OPERAND:
                return this.invalidOperandFunction;
            case InvalidConstant.INVALID_OPERATION:
                return this.invalidOperationFunction;
            case "ADD":
                return this.addFunction;
            case "SUBTRACT":
                return null;
            case "MULTIPLY":
                return null;
            case "DIVIDE":
                return null;
            default:
                return null;
        }
    }
}
