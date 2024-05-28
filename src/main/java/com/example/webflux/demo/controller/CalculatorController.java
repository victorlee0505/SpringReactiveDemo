package com.example.webflux.demo.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.example.webflux.demo.controller.model.CalculationRequest;
import com.example.webflux.demo.controller.model.CalculationResponse;
import com.example.webflux.demo.service.CalculatorService;
import com.example.webflux.demo.service.calculator.CalculatorFunctionFactory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CalculatorController implements CalApi {

    private final CalculatorService calculatorService;
    private final CalculatorFunctionFactory calculatorFunctionFactory;

    // @Override
    // public Mono<ResponseEntity<CalculationResponse>> calculateResult(@Valid Mono<CalculationRequest> calculationRequest,
    //         ServerWebExchange exchange) {
    //     return calculationRequest.map(request -> {
    //         try{
    //             BigDecimal result = calculatorService.calculateResult(request);
    //             CalculationResponse response = new CalculationResponse();
    //             response.setResult(result);
    //             response.setSuccess(true);
    //             return ResponseEntity.ok(response);
    //         } catch (Exception e) {
    //             CalculationResponse response = new CalculationResponse();
    //             response.setError(e.getMessage());
    //             response.setSuccess(false);
    //             return ResponseEntity.badRequest().body(response);
    //         }
    //     });
    // }

    @Override
    public Mono<ResponseEntity<CalculationResponse>> calculateResult(@Valid Mono<CalculationRequest> calculationRequest,
            ServerWebExchange exchange) {
        return calculationRequest.map(request -> {
            try {
                CalculationResponse response = calculatorFunctionFactory.getFunction(request).apply(request);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                CalculationResponse response = new CalculationResponse();
                response.setError(e.getMessage());
                response.setSuccess(false);
                return ResponseEntity.badRequest().body(response);
            }
        });
    }
    
}
