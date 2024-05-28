package com.example.webflux.demo.service.calculator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.webflux.demo.controller.model.CalculationRequest;
import com.example.webflux.demo.controller.model.CalculationResponse;
import com.example.webflux.demo.service.calculator.function.AddFunction;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CalculatorFunctionFactoryTest {


    private final CalculatorFunctionFactory calculatorFunctionFactory;
    
    @Test
    public void testGetFunction() {
        CalculationRequest request = new CalculationRequest();
        request.setOperator("+");
        request.setOperand1(new BigDecimal(3));
        request.setOperand2(new BigDecimal(2));

        Function<CalculationRequest, CalculationResponse> function = calculatorFunctionFactory.getFunction(request);

        assertTrue(function instanceof AddFunction);
        
    }

}
