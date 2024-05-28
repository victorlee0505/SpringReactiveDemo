package com.example.webflux.demo.service.calculator;

import java.util.Map;
import java.util.function.Predicate;

import com.example.webflux.demo.controller.model.CalculationRequest;

public class OperatorPredicate {
    public static final Predicate<CalculationRequest> ADD = request -> OperatorEnum.ADD.getSymbol().equals(request.getOperator());

    public static final Map<OperatorEnum, Predicate<CalculationRequest>> OPERATOR_MAP = Map.of(
        OperatorEnum.ADD, ADD
    );
}
