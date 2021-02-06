package ibaksheev;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class Executor {
    Scanner scanner;
    OutputStreamWriter output;
    Context ctx;
    CommandParser parser;
    CommandCreator creator;

    public Executor(InputStreamReader source, OutputStreamWriter output) {
        ctx = new Context();
        parser = new CommandParser();
        creator = new CommandCreator();
        scanner = new Scanner(source);
        this.output = output;
    }

    public void execute() throws IOException {
        while (scanner.hasNext()) {
            var line = scanner.nextLine();
            var parsedCommand = parser.parseLine(line);
            try {
                var command = creator.createCommand(parsedCommand.commandText);
                command.execute(ctx, parsedCommand.arguments);
            } catch (CalculatorException e) {
                output.write("error while executing command " + parsedCommand.commandText + ": " + e.getLocalizedMessage() + "\n");
            }
        }
    }
}
