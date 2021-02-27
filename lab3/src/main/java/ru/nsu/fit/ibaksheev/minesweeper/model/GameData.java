package ru.nsu.fit.ibaksheev.minesweeper.model;

public class GameData {
    FieldCellState[][] realField;
    FieldCellState[][] playerField;

    boolean firstShotDone = false;

    public static class Settings {
        private int width;
        private int height;
        private int minesCount;
        private String name;

        public Settings(int width, int height, int minesCount) {
            this.width = width;
            this.height = height;
            this.minesCount = minesCount;
        }

        public String getName() {
            return name;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getMinesCount() {
            return minesCount;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setMinesCount(int minesCount) {
            this.minesCount = minesCount;
        }
    }

    Settings settings;

    public GameData(Settings settings) {
        this.settings = settings;

        realField = new FieldCellState[settings.getWidth()][settings.getHeight()];
        playerField = new FieldCellState[settings.getWidth()][settings.getHeight()];

        for (int i = 0; i < settings.getWidth(); i++) {
            for (int j = 0; j < settings.getHeight(); j++) {
                playerField[i][j] = new FieldCellState(FieldCellState.Type.Unknown, 0);
                realField[i][j] = new FieldCellState(FieldCellState.Type.Unknown, 0);
            }
        }
    }
}
