package ru.nsu.fit.ibaksheev;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        WordStatsAnalyzer wordStatsAnalyzer = new WordStatsAnalyzer();
        if (args.length < 2) {
            System.err.println("Not enough arguments, at input and output files are required.");
        }
        try (
                InputStreamReader input = new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8);
                OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(args[1]))
        ) {
            wordStatsAnalyzer.countWordsToCSV(input, output);
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        }
    }
}
