package ru.nsu.fit.ibaksheev;

import ru.nsu.fit.ibaksheev.exceptions.CalculatorException;

import java.util.List;

public interface Command {
    void execute(Context ctx, List<String> arguments) throws CalculatorException;
}
