package ru.nsu.fit.ibaksheev;

import ru.nsu.fit.ibaksheev.exceptions.CalculatorException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;


import org.apache.log4j.Logger;

public class Executor {
    private final Scanner scanner;
    private final Context ctx;
    private final CommandParser parser;
    private final CommandCreator creator;

    private final Logger log = Logger.getLogger("GLOBAL");

    public Executor(InputStreamReader source, OutputStreamWriter output) {
        ctx = new Context(output);
        parser = new CommandParser();
        creator = new CommandCreator();
        scanner = new Scanner(source);
    }

    public void execute() throws IOException {
        log.info("execution started");
        while (scanner.hasNext()) {
            var line = scanner.nextLine();
            var parsedCommand = parser.parseLine(line);
            log.debug("parsed command: " + parsedCommand.commandText + " with arguments: " + parsedCommand.arguments);
            try {
                var command = creator.createCommand(parsedCommand.commandText);
                command.execute(ctx, parsedCommand.arguments);
            } catch (CalculatorException e) {
                log.error("error while executing command " + parsedCommand.commandText + ": " + e.getLocalizedMessage());
            }
        }
        log.info("execution completed");
    }
}