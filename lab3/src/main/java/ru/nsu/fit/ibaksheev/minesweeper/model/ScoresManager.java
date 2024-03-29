package ru.nsu.fit.ibaksheev.minesweeper.model;

import lombok.Getter;
import org.joda.time.Duration;
import ru.nsu.fit.ibaksheev.minesweeper.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScoresManager implements Serializable {
    @Getter
    private final List<ScoreEntry> entries;

    public ScoresManager() {
        entries = new ArrayList<>();
    }

    public void addEntry(ScoreEntry entry) {
        entries.add(entry);
    }

    public static class ScoreEntry implements Serializable {
        private final SettingsManager.Settings settings;
        private final Duration duration;

        public ScoreEntry(SettingsManager.Settings settings, Duration duration) {
            this.settings = settings;
            this.duration = duration;
        }

        public String getSettingsName() {
            return settings.getName();
        }

        public String getDurationString() {
            return Utils.formatDuration(duration);
        }
    }
}
