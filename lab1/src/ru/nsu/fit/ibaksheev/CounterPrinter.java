package ru.nsu.fit.ibaksheev;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.util.Locale;

public class CounterPrinter {
    private final OutputStreamWriter writer;

    public CounterPrinter(OutputStreamWriter writer) {
        this.writer = writer;
    }

    public <T> void printCounter(Counter<T> counter) {
        counter.mostCommon().forEach((entry) -> {
            try {
                writer.write(entry.getKey() + "," + entry.getValue() + "," + String.format(Locale.US, "%.2f", entry.getValue() * 100.0 / counter.getTotal()) + "\n");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
