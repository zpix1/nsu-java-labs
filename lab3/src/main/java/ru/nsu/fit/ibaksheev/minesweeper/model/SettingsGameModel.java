package ru.nsu.fit.ibaksheev.minesweeper.model;

import lombok.Getter;

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
