package ru.nsu.fit.ibaksheev.minesweeper.model;

public class GameData {
    FieldCellState[][] realField;
    FieldCellState[][] playerField;

    boolean firstShotDone = false;

    int width, height, mineCount;

    public GameData(int width, int height, int mineCount) {
        this.width = width;
        this.height = height;
        this.mineCount = mineCount;

        realField = new FieldCellState[width][height];
        playerField = new FieldCellState[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                playerField[i][j] = new FieldCellState(FieldCellState.Type.Unknown, 0);
                realField[i][j] = new FieldCellState(FieldCellState.Type.Unknown, 0);
            }
        }
    }
}
