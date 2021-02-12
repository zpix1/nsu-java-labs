package ru.nsu.fit.ibaksheev.lab2.exceptions;

public class CalculatorCommandException extends CalculatorException{
    public CalculatorCommandException(String errorMessage) {
        super(errorMessage);
    }
}
