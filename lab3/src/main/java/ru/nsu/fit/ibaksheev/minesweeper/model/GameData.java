package ru.nsu.fit.ibaksheev.minesweeper.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

// Is manipulated by GameModel
@Data
public class GameData implements Serializable {
    private FieldCellState[][] realField;
    private FieldCellState[][] playerField;

    private boolean firstShotDone = false;

    private Date startedAt;

    private SettingsManager.Settings settings;
    private State state = State.NOT_STARTED;

    public GameData(SettingsManager.Settings settings) {
        this.setSettings(settings);
        setStartedAt(new Date());

        setRealField(new FieldCellState[settings.getWidth()][settings.getHeight()]);
        setPlayerField(new FieldCellState[settings.getWidth()][settings.getHeight()]);

        for (int i = 0; i < settings.getWidth(); i++) {
            for (int j = 0; j < settings.getHeight(); j++) {
                getPlayerField()[i][j] = new FieldCellState(FieldCellState.Type.Unknown, 0);
                getRealField()[i][j] = new FieldCellState(FieldCellState.Type.Unknown, 0);
            }
        }
    }

    public enum State {
        NOT_STARTED,
        STARTED,
        WON,
        LOST
    }
}
