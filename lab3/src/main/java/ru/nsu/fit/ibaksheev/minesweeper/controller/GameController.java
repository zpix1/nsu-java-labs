package ru.nsu.fit.ibaksheev.minesweeper.controller;

import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
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

    public void shoot(int x, int y) throws InvalidArgumentException {
        currentModel.shoot(x, y);
    }

    public void flag(int x, int y) throws InvalidArgumentException {
        currentModel.flag(x, y);
    }

    public void newGame(GameData.Settings settings) {
        var data = new GameData(settings);
        currentModel.setGameData(data);
    }
}
