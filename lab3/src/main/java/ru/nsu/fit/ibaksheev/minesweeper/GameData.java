package ru.nsu.fit.ibaksheev.minesweeper;

public class GameData {
    enum Type {
        Empty,
        Unknown,
        Flag,
        Mine
    };

    static class FieldCellState {
        Type type;
        int value;
    }

    private FieldCellState[][] realField;
    private FieldCellState[][] playerField;

    boolean firstShotDone = false;

    int width, height, mineCount;

    GameData(int width, int height, int mineCount) {
        this.width = width;
        this.height = height;
        this.mineCount = mineCount;

        realField = new FieldCellState[width][height];
        playerField = new FieldCellState[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                playerField[i][j].type = Type.Unknown;
                realField[i][j].type = Type.Unknown;
            }
        }
    }
}
