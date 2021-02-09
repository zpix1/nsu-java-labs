package ru.nsu.fit.ibaksheev;

import java.util.List;

public class ParsedCommand {
    String commandText;
    List<String> arguments;

    public ParsedCommand(String commandText, List<String> arguments) {
        this.commandText = commandText;
        this.arguments = arguments;
    }
}

