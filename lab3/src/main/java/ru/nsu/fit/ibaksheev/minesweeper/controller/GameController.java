package ru.nsu.fit.ibaksheev.minesweeper.controller;

import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

public interface GameController {
    GameModel getModel();

    void flag(int x, int y) throws InvalidArgumentException;

    void shoot(int x, int y) throws InvalidArgumentException;
}
