package ibaksheev.commands;

import java.util.regex.Pattern;

public class Utils {
    public static TokenType parseToken(String string) {
        if (Pattern.matches("(\\d+)?\\.(\\d+)?", string)) {
            return TokenType.Digits;
        } else if (Pattern.matches("[a-zA-Z]+", string)) {
            return TokenType.Macro;
        }
        return TokenType.Unknown;
    }
}
