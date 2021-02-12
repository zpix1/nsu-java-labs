package ru.nsu.fit.ibaksheev.lab2;

import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorException;

import java.util.List;

public interface Command {
    void execute(Context ctx, List<String> arguments) throws CalculatorException;
}
