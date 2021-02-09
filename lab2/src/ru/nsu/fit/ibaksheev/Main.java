package ru.nsu.fit.ibaksheev;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        InputStreamReader input;
        OutputStreamWriter output = new OutputStreamWriter(System.out);
        if (args.length == 1) {
            try {
                input = new InputStreamReader(new FileInputStream(args[0]));
            } catch (FileNotFoundException e) {
                System.err.println(e.getLocalizedMessage());
                return;
            }
        } else {
            input = new InputStreamReader(System.in);
        }
        var executor = new Executor(input, output);
        try {
            executor.execute();
            output.close();
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
