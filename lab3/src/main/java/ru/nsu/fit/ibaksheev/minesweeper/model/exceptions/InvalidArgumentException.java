package ru.nsu.fit.ibaksheev.minesweeper.model.exceptions;

public class InvalidArgumentException extends GameException {
    public InvalidArgumentException(String errorMessage) {
        super(errorMessage);
    }
}
