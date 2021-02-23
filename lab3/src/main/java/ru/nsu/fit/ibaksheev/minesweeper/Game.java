package ru.nsu.fit.ibaksheev.minesweeper;

public class Game {
    public enum FieldState {
        Mine,
        Empty,
        Flag
    }

    private RealFieldState[][] realField;
    private PlayerFieldState[][] playerField;


}
