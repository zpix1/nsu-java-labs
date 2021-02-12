package ru.nsu.fit.ibaksheev.lab2.commands;


import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorArgumentException;
import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorException;
import ru.nsu.fit.ibaksheev.lab2.Command;
import ru.nsu.fit.ibaksheev.lab2.Context;

import java.util.List;

public class PushCommand implements Command {
    @Override
    public void execute(Context ctx, List<String> arguments) throws CalculatorException {
        if (arguments.size() != 1) {
            throw new CalculatorArgumentException("wrong amount of arguments for command");
        }
        var operand = arguments.get(0);
        ctx.push(Utils.getValueWithMacro(operand, ctx));
    }
}
