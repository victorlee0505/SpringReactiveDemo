package com.example.webflux.demo.service.calculator.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.webflux.demo.controller.model.CalculationRequest;
import com.example.webflux.demo.controller.model.CalculationResponse;

@SpringBootTest
public class AddFunctionTest {

    @Autowired
    private AddFunction addFunction;

    @Test
    public void testApply() {
        CalculationRequest request = new CalculationRequest();
        request.setOperator("+");
        request.setOperand1(new BigDecimal(2));
        request.setOperand2(new BigDecimal(2));
        CalculationResponse response = addFunction.apply(request);

        CalculationResponse expectedResponse = new CalculationResponse();
        expectedResponse.setResult(new BigDecimal(4));
        expectedResponse.setSuccess(true);

        assertEquals(expectedResponse, response);
    }
}
