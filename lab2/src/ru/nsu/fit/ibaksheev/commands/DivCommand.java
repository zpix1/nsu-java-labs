package ru.nsu.fit.ibaksheev.commands;

import ru.nsu.fit.ibaksheev.exceptions.CalculatorArgumentException;
import ru.nsu.fit.ibaksheev.exceptions.CalculatorException;
import ru.nsu.fit.ibaksheev.Command;
import ru.nsu.fit.ibaksheev.Context;
import ru.nsu.fit.ibaksheev.exceptions.CalculatorMathException;
import ru.nsu.fit.ibaksheev.exceptions.CalculatorStackException;

import java.util.List;

public class DivCommand implements Command {
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
        if (v2 == 0) {
            ctx.push(v2);
            ctx.push(v1);
            throw new CalculatorMathException("zero division error");
        }
        ctx.push(v1 / v2);
    }
}