package ru.nsu.fit.ibaksheev.commands;

import ru.nsu.fit.ibaksheev.exceptions.CalculatorArgumentException;
import ru.nsu.fit.ibaksheev.exceptions.CalculatorException;
import ru.nsu.fit.ibaksheev.Context;

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
