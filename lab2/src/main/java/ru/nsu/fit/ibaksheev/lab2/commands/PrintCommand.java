package ru.nsu.fit.ibaksheev.lab2.commands;

import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorArgumentException;
import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorException;
import ru.nsu.fit.ibaksheev.lab2.Command;
import ru.nsu.fit.ibaksheev.lab2.Context;
import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorStackException;

import java.io.IOException;
import java.util.List;

public class PrintCommand implements Command {
    @Override
    public void execute(Context ctx, List<String> arguments) throws CalculatorException {
        if (arguments.size() != 0) {
            throw new CalculatorArgumentException("wrong amount of arguments for command");
        }
        if (ctx.size() < 1) {
            throw new CalculatorStackException("not enough values on the stack");
        }
        var v = ctx.peek();
        try {
            ctx.print(v);
        } catch (IOException e) {
            throw new CalculatorException("can't print: " + e.getLocalizedMessage());
        }
    }
}
