package tests;

import ibaksheev.calculator.Executor;
import org.junit.Test;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;
public class CalculatorExecutorTests {
    @Test
    public void validProgramWorks() throws IOException {
        var instructions = "DEFINE A 5\nDEFINE B 6\nPUSH A\nPUSH B\nPUSH 10\n+\nSQRT\n*\nPRINT";
        var out = new ByteArrayOutputStream();
        var outWriter = new OutputStreamWriter(out);
        var executor = new Executor(new InputStreamReader(new ByteArrayInputStream(instructions.getBytes())), outWriter);
        executor.execute();
        outWriter.close();
        var result = out.toString();
        assertThat(result, CoreMatchers.containsString("20.0"));
    }
}
