package ru.nsu.fit.ibaksheev.lab2.commands;

import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorArgumentException;
import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorException;
import ru.nsu.fit.ibaksheev.lab2.Command;
import ru.nsu.fit.ibaksheev.lab2.Context;

import java.util.List;

public class DefineCommand implements Command {
    @Override
    public void execute(Context ctx, List<String> arguments) throws CalculatorException {
        if (arguments.size() != 2) {
            throw new CalculatorArgumentException("wrong amount of arguments for command");
        }
        var tokenType1 = Utils.parseToken(arguments.get(0));
        var tokenType2 = Utils.parseToken(arguments.get(1));

        if (tokenType1 != TokenType.Macro || tokenType2 != TokenType.Digits) {
            throw new CalculatorArgumentException("invalid argument for define");
        }

        ctx.define(arguments.get(0), Double.valueOf(arguments.get(1)));
    }
}
