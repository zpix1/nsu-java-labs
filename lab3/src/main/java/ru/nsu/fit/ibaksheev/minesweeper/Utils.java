package ru.nsu.fit.ibaksheev.minesweeper;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Date;

public class Utils {
    private static PeriodFormatter minutesAndSeconds = new PeriodFormatterBuilder()
            .printZeroAlways()
            .minimumPrintedDigits(2)
            .appendMinutes()
            .appendSeparator(":")
            .minimumPrintedDigits(2)
            .appendSeconds()
            .toFormatter();

    public static String formatDuration(Duration duration) {
        Period period = duration.toPeriod();
        return minutesAndSeconds.print(period);
    }
}
