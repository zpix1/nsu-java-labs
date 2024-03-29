package ru.nsu.fit.ibaksheev.lab2;

import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorDefineException;
import ru.nsu.fit.ibaksheev.lab2.exceptions.CalculatorException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Context {
    private final Stack<Double> stack;
    private final Map<String, Double> defines;
    private final OutputStreamWriter output;

    public Context(OutputStreamWriter output) {
        stack = new Stack<>();
        defines = new HashMap<>();
        this.output = output;
    }

    public void push(Double value) {
        stack.push(value);
    }

    public Double pop() {
        return stack.pop();
    }

    public void define(String macros, Double value) throws CalculatorException {
        if (defines.containsKey(macros)) {
            throw new CalculatorDefineException("macros redefinition is not possible");
        }
        defines.put(macros, value);
    }

    public Double load(String macros) throws CalculatorException {
        var res = defines.get(macros);
        if (res == null) {
            throw new CalculatorDefineException("macros is not defined");
        }
        return res;
    }

    public Integer size() {
        return stack.size();
    }

    public Double peek() {
        return stack.peek();
    }

    public void print(Double value) throws IOException {
        // should be changed to crossplatform version
        output.write(value + "\n");
        output.flush();
    }
}
