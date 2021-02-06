package ibaksheev;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandCreator {
    Map<String, String> commandClassNames;

    public CommandCreator() {
        var scanner = new Scanner(this.getClass().getResourceAsStream("commands.conf")).useDelimiter(" ");
        commandClassNames = new HashMap<>();
        while (scanner.hasNext()) {
            commandClassNames.put(scanner.next(), scanner.next());
        }
    }

    Command createCommand(String name) throws CalculatorException {
        var className = commandClassNames.get(name);

        try {
            return (Command) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new CalculatorException("invalid class");
        }
    }
}
