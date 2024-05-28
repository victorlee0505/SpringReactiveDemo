package com.example.webflux.demo.service.calculator;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.example.webflux.demo.controller.model.CalculationRequest;

public class CalculationRequestPredicate {

    public static final List<OperatorEnum> operators = Arrays.asList(OperatorEnum.values());

    public static final Predicate<CalculationRequest> IS_INVALID_OPERATOR = request -> request.getOperator() == null
            || request.getOperator().isEmpty() || notSupportedOperator(request.getOperator());

    public static final Predicate<CalculationRequest> IS_INVALID_OPERAND = request -> request.getOperand1() == null || request.getOperand2() == null;

    private static final boolean notSupportedOperator(String operator) {
        return !operators.contains(OperatorEnum.fromSymbol(operator));
    }
}