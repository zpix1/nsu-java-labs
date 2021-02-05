package ibaksheev;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

class WordStatsAnalyzer {
    public void countWordsToCSV(InputStreamReader source, OutputStreamWriter destination) throws IOException {
        Counter<String> counter = new Counter<>();
        Scanner scanner = new Scanner(source).useDelimiter("[\\P{L}0-9]+");

        while (scanner.hasNext()) {
            counter.add(scanner.next());
        }

        CounterPrinter printer = new CounterPrinter(destination);

        printer.printCounter(counter);
    }
}
