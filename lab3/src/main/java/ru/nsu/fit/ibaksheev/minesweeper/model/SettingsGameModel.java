package ru.nsu.fit.ibaksheev.minesweeper.model;

import lombok.Getter;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class SettingsGameModel extends GameModel {
    @Getter
    private final ScoresManager scoresManager;

    public SettingsGameModel() {
        super();
        scoresManager = new ScoresManager();
    }

    public SettingsManager.Settings getSettings() {
        return getProperty().getSettings();
    }
}
