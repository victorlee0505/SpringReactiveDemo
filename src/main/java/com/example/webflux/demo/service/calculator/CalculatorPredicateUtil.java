package com.example.webflux.demo.service.calculator;

import java.util.Map;
import java.util.function.Predicate;

import com.example.webflux.demo.controller.model.CalculationRequest;

public class CalculatorPredicateUtil {

    public static String findOperation(CalculationRequest request) {

        if(CalculationRequestPredicate.IS_INVALID_OPERAND.test(request)) {
            return InvalidConstant.INVALID_OPERAND;
        }
        
        if(CalculationRequestPredicate.IS_INVALID_OPERATOR.test(request)) {
            return InvalidConstant.INVALID_OPERATOR;
        }

        for (Map.Entry<OperatorEnum, Predicate<CalculationRequest>> entry : OperatorPredicate.OPERATOR_MAP.entrySet()) {
            if (entry.getValue().test(request)) {
                return entry.getKey().name();
            }
        }

        // this should never happen
        return InvalidConstant.INVALID_OPERATION;
    }
}
