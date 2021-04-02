package ru.nsu.fit.ibaksheev.minesweeper.view;

import ru.nsu.fit.ibaksheev.minesweeper.controller.LocalGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.SettingsGameModel;

public abstract class GameView implements View<GameData> {
    protected final SettingsGameModel model;
    protected final LocalGameController controller;

    public GameView(SettingsGameModel model, LocalGameController controller) {
        this.model = model;
        this.controller = controller;
    }

    public abstract void start();
}
