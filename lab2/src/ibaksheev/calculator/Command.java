package ibaksheev.calculator;

import ibaksheev.calculator.exceptions.CalculatorException;

import java.util.List;

public interface Command {
    void execute(Context ctx, List<String> arguments) throws CalculatorException;
}
