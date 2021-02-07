package ibaksheev.calculator;

import ibaksheev.calculator.exceptions.CalculatorException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;


import org.apache.log4j.Logger;

public class Executor {
    Logger log = Logger.getLogger("GLOBAL");

    Scanner scanner;
    OutputStreamWriter output;
    Context ctx;
    CommandParser parser;
    CommandCreator creator;

    public Executor(InputStreamReader source, OutputStreamWriter output) {
        ctx = new Context(output);
        parser = new CommandParser();
        creator = new CommandCreator();
        scanner = new Scanner(source);
        this.output = output;
    }

    public void execute() throws IOException {
        while (scanner.hasNext()) {
            var line = scanner.nextLine();
            var parsedCommand = parser.parseLine(line);
            log.debug("parsed command: " + parsedCommand.commandText + " with arguments: " + parsedCommand.arguments);
            try {
                var command = creator.createCommand(parsedCommand.commandText);
                command.execute(ctx, parsedCommand.arguments);
            } catch (CalculatorException e) {
                output.write("error while executing command " + parsedCommand.commandText + ": " + e.getLocalizedMessage() + "\n");
            }
        }
    }
}
