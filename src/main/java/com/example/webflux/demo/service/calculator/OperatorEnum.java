package com.example.webflux.demo.service.calculator;

public enum OperatorEnum {
    ADD("+");

    private final String symbol;

    OperatorEnum(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public static OperatorEnum fromSymbol(String symbol) {
        for (OperatorEnum operator : OperatorEnum.values()) {
            if (operator.getSymbol().equals(symbol)) {
                return operator;
            }
        }
        return null;
    }
}
