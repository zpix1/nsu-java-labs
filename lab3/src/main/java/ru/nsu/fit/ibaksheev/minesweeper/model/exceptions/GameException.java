package ru.nsu.fit.ibaksheev.minesweeper.model.exceptions;

public class GameException extends Exception {
    public GameException(String errorMessage) {
        super(errorMessage);
    }
}
