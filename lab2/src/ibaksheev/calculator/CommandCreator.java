package ibaksheev.calculator;

import ibaksheev.calculator.exceptions.CalculatorCommandException;
import ibaksheev.calculator.exceptions.CalculatorException;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandCreator {
    Map<String, String> commandClassNames;
    static final String commandsResource = "/commands.conf";

    Logger log = Logger.getLogger("GLOBAL");

    public CommandCreator() {
        var scanner = new Scanner(this.getClass().getResourceAsStream(commandsResource)).useDelimiter("[ \n]");
        commandClassNames = new HashMap<>();
        while (scanner.hasNext()) {
            commandClassNames.put(scanner.next(), scanner.next());
        }
    }

    Command createCommand(String name) throws CalculatorException {
        var className = commandClassNames.get(name);

        if (className == null) {
            throw new CalculatorCommandException("command not found in " + commandsResource);
        }
        log.debug("creating class from " + className.equals("ibaksheev.calculator.commands.PushCommand\n"));
        try {
            return (Command) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | NullPointerException | InvocationTargetException | ClassNotFoundException e) {
            log.error("got exception while creating command from " + className);
            e.printStackTrace();
            throw new CalculatorCommandException("invalid class");
        }
    }
}
