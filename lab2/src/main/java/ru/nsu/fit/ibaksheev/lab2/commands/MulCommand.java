package ru.nsu.fit.ibaksheev.lab2.commands;

import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorArgumentException;
import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorException;
import ru.nsu.fit.ibaksheev.lab2.Command;
import ru.nsu.fit.ibaksheev.lab2.Context;
import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorStackException;

import java.util.List;

public class MulCommand implements Command {
    @Override
    public void execute(Context ctx, List<String> arguments) throws CalculatorException {
        if (arguments.size() != 0) {
            throw new CalculatorArgumentException("wrong amount of arguments for command");
        }
        if (ctx.size() < 2) {
            throw new CalculatorStackException("not enough values on the stack");
        }
        var v1 = ctx.pop();
        var v2 = ctx.pop();
        ctx.push(v1 * v2);
    }
}
