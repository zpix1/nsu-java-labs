package ibaksheev;

import java.util.List;

public interface Command {
    void execute(Context ctx, List<String> arguments) throws CalculatorException;
}
