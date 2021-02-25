package ru.nsu.fit.ibaksheev.minesweeper.controller;

import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.GameException;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

public class GameController {
    GameModel currentModel;

    public GameController(GameModel model) {
        if (model == null) {
            throw new NullPointerException("Null model");
        }
        currentModel = model;
    }

    public void shot(int x, int y) throws InvalidArgumentException {
        currentModel.shot(x, y);
    }
}
