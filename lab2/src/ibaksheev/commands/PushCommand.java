package ibaksheev.commands;


import ibaksheev.CalculatorException;
import ibaksheev.Command;
import ibaksheev.Context;

import java.util.List;

public class PushCommand implements Command {

    @Override
    public void execute(Context ctx, List<String> arguments) throws CalculatorException {
        if (arguments.size() != 1) {
            throw new CalculatorException("wrong amount of arguments for command");
        }
        var operand = arguments.get(0);
        var tokenType = Utils.parseToken(operand);
        if (tokenType == TokenType.Unknown) {
            throw new CalculatorException("invalid argument");
        } else if (tokenType == TokenType.Digits) {
            ctx.push(Double.valueOf(operand));
        } else if (tokenType == TokenType.Macro) {
            var value = ctx.load(operand);
            ctx.push(value);
        }
    }
}
