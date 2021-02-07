package ibaksheev.calculator.commands;


import ibaksheev.calculator.exceptions.CalculatorArgumentException;
import ibaksheev.calculator.exceptions.CalculatorException;
import ibaksheev.calculator.Command;
import ibaksheev.calculator.Context;

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
