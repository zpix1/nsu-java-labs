package tests;

import ibaksheev.calculator.exceptions.CalculatorDefineException;
import ibaksheev.calculator.exceptions.CalculatorException;
import ibaksheev.calculator.Context;
import ibaksheev.calculator.commands.*;
import ibaksheev.calculator.exceptions.CalculatorMathException;
import ibaksheev.calculator.exceptions.CalculatorStackException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CalculatorCommandTests {
    Context getDefaultContext() throws CalculatorException {
        var ctx = new Context(new OutputStreamWriter(OutputStream.nullOutputStream()));
        ctx.define("A", 5.0);
        return ctx;
    }

    @Test
    public void popWorks() throws CalculatorException {
        var ctx = getDefaultContext();
        var pop = new PopCommand();
        ctx.push(5.0);
        ctx.push(10.0);
        pop.execute(ctx, new ArrayList<>());
        pop.execute(ctx, new ArrayList<>());
        assertEquals((Object) 0, ctx.size());
    }

    @Test(expected = CalculatorStackException.class)
    public void popFails() throws CalculatorException {
        var ctx = getDefaultContext();
        var pop = new PopCommand();
        pop.execute(ctx, new ArrayList<>());
    }

    @Test
    public void pushWorks() throws CalculatorException {
        var ctx = getDefaultContext();
        var push = new PushCommand();
        push.execute(ctx, new ArrayList<>(List.of("A")));
        push.execute(ctx, new ArrayList<>(List.of("11")));
        assertEquals((Object) 11.0, ctx.pop());
        assertEquals((Object) 5.0, ctx.pop());
    }

    @Test
    public void mathWorks() throws CalculatorException {
        var ctx = getDefaultContext();
        ctx.push(5.0);
        ctx.push(6.0);
        (new AddCommand()).execute(ctx, new ArrayList<>());
        assertEquals((Object) 11.0, ctx.pop());
        assertEquals((Object) 0, ctx.size());

        ctx.push(5.0);
        ctx.push(6.0);
        (new SubCommand()).execute(ctx, new ArrayList<>());
        assertEquals((Object) (1.0), ctx.pop());
        assertEquals((Object) 0, ctx.size());

        ctx.push(5.0);
        ctx.push(6.0);
        (new MulCommand()).execute(ctx, new ArrayList<>());
        assertEquals((Object) (30.0), ctx.pop());
        assertEquals((Object) 0, ctx.size());

        ctx.push(6.0);
        ctx.push(30.0);
        (new DivCommand()).execute(ctx, new ArrayList<>());
        assertEquals((Object) (5.0), ctx.pop());
        assertEquals((Object) 0, ctx.size());

        ctx.push(25.0);
        (new SqrtCommand()).execute(ctx, new ArrayList<>());
        assertEquals((Object) (5.0), ctx.pop());
        assertEquals((Object) 0, ctx.size());
    }

    @Test
    public void zeroDivisionFails() throws CalculatorException {
        var ctx = getDefaultContext();
        ctx.push(0.0);
        ctx.push(6.0);
        try {
            (new DivCommand()).execute(ctx, new ArrayList<>());
        } catch (CalculatorMathException e) {
            assertEquals((Object) (6.0), ctx.pop());
            assertEquals((Object) (0.0), ctx.pop());
        }
    }

    @Test
    public void negativeSqrtFails() throws CalculatorException {
        var ctx = getDefaultContext();
        ctx.push(-25.0);
        try {
            (new SqrtCommand()).execute(ctx, new ArrayList<>());
        } catch (CalculatorMathException e) {
            assertEquals((Object) (-25.0), ctx.pop());
        }
    }

    @Test
    public void printWorks() throws CalculatorException, IOException {
        var out = new ByteArrayOutputStream();
        var outWriter = new OutputStreamWriter(out);
        var ctx = new Context(outWriter);

        ctx.push(5.0);
        (new PrintCommand()).execute(ctx, new ArrayList<>());
        outWriter.close();

        var result = out.toString();
        assertThat(result, CoreMatchers.containsString("5.0\n"));
    }

    @Test
    public void defineWorks() throws CalculatorException {
        var ctx = getDefaultContext();
        (new DefineCommand()).execute(ctx, new ArrayList<>(List.of("VAR", "5.0")));
        assertEquals((Object) 5.0, ctx.load("VAR"));
    }

    @Test(expected = CalculatorDefineException.class)
    public void defineDefinedFails() throws CalculatorException {
        var ctx = getDefaultContext();
        (new DefineCommand()).execute(ctx, new ArrayList<>(List.of("VAR", "5.0")));
        (new DefineCommand()).execute(ctx, new ArrayList<>(List.of("VAR", "5.0")));
    }

    @Test(expected = CalculatorDefineException.class)
    public void pushUndefinedFails() throws CalculatorException {
        var ctx = getDefaultContext();
        (new PushCommand()).execute(ctx, new ArrayList<>(List.of("UNDEF_VAR")));
    }

    @Test
    public void commentWorks() throws CalculatorException {
        var ctx = getDefaultContext();
        var commentCommand = new CommentCommand();
        commentCommand.execute(ctx, new ArrayList<>(List.of("123", "321")));
        assertEquals((Object) 0, ctx.size());
    }
}
