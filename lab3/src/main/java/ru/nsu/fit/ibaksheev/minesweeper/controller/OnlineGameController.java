package ru.nsu.fit.ibaksheev.minesweeper.controller;

import lombok.Getter;
import ru.nsu.fit.ibaksheev.minesweeper.model.*;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

public class OnlineGameController implements GameController {
    @Getter
    GameModel syncModel;
    @Getter
    GameModel model;

    public OnlineGameController() {
        syncModel = new GameModel();
        syncModel.setGameData(new GameData(new SettingsManager.Settings(10, 10, 10)));
        model = new GameModel();
        model.setGameData(new GameData(new SettingsManager.Settings(10, 10, 10)));
    }

    @Override
    public void shoot(int x, int y) throws InvalidArgumentException {
        model.shoot(x, y);
    }

    @Override
    public void flag(int x, int y) throws InvalidArgumentException {
        model.flag(x, y);
    }
}
