package ru.nsu.fit.ibaksheev.lab2;

import java.util.ArrayList;
import java.util.Scanner;

public class CommandParser {
    ParsedCommand parseLine(String line) {
        var scanner = new Scanner(line).useDelimiter(" ");
        var commandText = scanner.next();
        var list = new ArrayList<String>();

        while (scanner.hasNext()) {
            list.add(scanner.next());
        }

        return new ParsedCommand(commandText, list);
    }
}
