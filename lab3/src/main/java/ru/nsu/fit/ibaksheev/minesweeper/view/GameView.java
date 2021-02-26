package ru.nsu.fit.ibaksheev.minesweeper.view;

import ru.nsu.fit.ibaksheev.minesweeper.controller.GameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;

public abstract class GameView implements View<GameData> {
    final protected GameModel model;
    final protected GameController controller;

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
    }

    abstract public void start();
}
