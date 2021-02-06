package ibaksheev;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Context {
    Stack<Double> stack;
    Map<String, Double> defines;

    public Context() {
        this.stack = new Stack<>();
        this.defines = new HashMap<>();
    }

    public void push(Double value) {
        this.stack.push(value);
    }

    public Double pop() {
        return this.stack.pop();
    }

    public void define(String macros, Double value) {
        this.defines.put(macros, value);
    }

    public Double load(String macros) throws CalculatorException {
        var res = this.defines.get(macros);
        if (res == null) {
            throw new CalculatorException("macros is not defined");
        }
        return res;
    }
}
