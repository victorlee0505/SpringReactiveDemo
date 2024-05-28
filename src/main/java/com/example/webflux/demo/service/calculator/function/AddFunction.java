package com.example.webflux.demo.service.calculator.function;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.example.webflux.demo.controller.model.CalculationRequest;
import com.example.webflux.demo.controller.model.CalculationResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AddFunction implements Function<CalculationRequest, CalculationResponse> {

    Function<CalculationRequest, CalculationResponse> func = (request) -> {
        log.info("Add function called");
        CalculationResponse response = new CalculationResponse();
        response.setResult(request.getOperand1().add(request.getOperand2()));
        response.setSuccess(true);
        return response;
    };

    @Override
    public CalculationResponse apply(CalculationRequest t) {
        return this.func.apply(t);
    }

}
