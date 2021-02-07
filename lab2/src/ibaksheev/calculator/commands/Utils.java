package ibaksheev.calculator.commands;

import ibaksheev.calculator.exceptions.CalculatorArgumentException;
import ibaksheev.calculator.exceptions.CalculatorException;
import ibaksheev.calculator.Context;

import java.util.regex.Pattern;

public class Utils {
    public static TokenType parseToken(String string) {
        if (Pattern.matches("[+-]?([0-9]*[.])?[0-9]+", string)) {
            return TokenType.Digits;
        }
        if (Pattern.matches("[a-zA-Z_]+", string)) {
            return TokenType.Macro;
        }
        return TokenType.Unknown;
    }

    public static Double getValueWithMacro(String string, Context ctx) throws CalculatorException {
        var tokenType = parseToken(string);
        if (tokenType == TokenType.Digits) {
            return Double.valueOf(string);
        }
        if (tokenType == TokenType.Macro) {
            return ctx.load(string);
        }
        throw new CalculatorArgumentException("invalid argument: " + string);
    }
}
