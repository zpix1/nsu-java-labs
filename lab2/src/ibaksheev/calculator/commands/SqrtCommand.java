package ibaksheev.calculator.commands;

import ibaksheev.calculator.exceptions.CalculatorArgumentException;
import ibaksheev.calculator.exceptions.CalculatorException;
import ibaksheev.calculator.Command;
import ibaksheev.calculator.Context;
import ibaksheev.calculator.exceptions.CalculatorMathException;
import ibaksheev.calculator.exceptions.CalculatorStackException;

import java.util.List;

public class SqrtCommand implements Command {
    @Override
    public void execute(Context ctx, List<String> arguments) throws CalculatorException {
        if (arguments.size() != 0) {
            throw new CalculatorArgumentException("wrong amount of arguments for command");
        }
        if (ctx.size() < 1) {
            throw new CalculatorStackException("not enough values on the stack");
        }
        var v = ctx.pop();
        if (v < 0) {
            ctx.push(v);
            throw new CalculatorMathException("negative sqrt is not supported");
        }
        ctx.push(Math.sqrt(v));
    }
}
