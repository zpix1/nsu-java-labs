package ibaksheev;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CounterPrinter {
    OutputStreamWriter writer;

    public CounterPrinter(OutputStreamWriter writer) {
        this.writer = writer;
    }

    public <T> void printCounter(Counter<T> counter) {
        counter.mostCommon().forEach((entry) -> {
            try {
                writer.write(entry.getKey() + "," + entry.getValue() + "," + String.format(Locale.US, "%.2f", entry.getValue() * 100.0 / counter.total) + "\n");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
