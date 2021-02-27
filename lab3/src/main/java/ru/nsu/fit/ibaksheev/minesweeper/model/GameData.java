package ru.nsu.fit.ibaksheev.minesweeper.model;

import java.io.Serializable;
import java.util.Date;

// Is manipulated by GameModel
public class GameData implements Serializable {
    FieldCellState[][] realField;
    FieldCellState[][] playerField;

    boolean firstShotDone = false;

    Date startedAt;

    SettingsManager.Settings settings;

    public enum State {
        NOT_STARTED,
        STARTED,
        WON,
        LOST
    }

    State state = State.NOT_STARTED;

    public GameData(SettingsManager.Settings settings) {
        this.settings = settings;
        startedAt = new Date();

        realField = new FieldCellState[settings.getWidth()][settings.getHeight()];
        playerField = new FieldCellState[settings.getWidth()][settings.getHeight()];

        for (int i = 0; i < settings.getWidth(); i++) {
            for (int j = 0; j < settings.getHeight(); j++) {
                playerField[i][j] = new FieldCellState(FieldCellState.Type.Unknown, 0);
                realField[i][j] = new FieldCellState(FieldCellState.Type.Unknown, 0);
            }
        }
    }
}
