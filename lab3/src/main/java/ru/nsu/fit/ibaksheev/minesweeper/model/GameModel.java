package ru.nsu.fit.ibaksheev.minesweeper.model;

import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.GameException;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

import java.util.*;

public class GameModel extends Model<GameData> {
    // TODO: does it break SRP?
    public SettingsManager settingsManager = new SettingsManager("/settings.properties");

    private static final int[][] NEIGHBOURS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    public GameModel() {
        super(null);
    }

    public void setGameData(GameData data) {
        this.setProperty(data);
        notifySubscribers("reset");
    }

    private boolean checkCoordinates(int x, int y) {
        var data = getProperty();
        return 0 <= x && x < data.settings.getWidth() && 0 <= y && y < data.settings.getHeight();
    }

    private void checkCoordinatesWithException(int x, int y) throws InvalidArgumentException {
        if (checkCoordinates(x, y)) {
            return;
        }
        throw new InvalidArgumentException("invalid coordinates: " + x + " " + y);
    }

    public void shoot(int x, int y) throws InvalidArgumentException {
        checkCoordinatesWithException(x, y);
        var data = getProperty();
        if (!data.firstShotDone) {
            fillFields(x, y);
            data.playerField[x][y].type = FieldCellState.Type.Empty;
            emptyFieldDFS(x, y);
            data.firstShotDone = true;
        } else {
            if (data.realField[x][y].type == FieldCellState.Type.Mine) {
                data.playerField[x][y].type = FieldCellState.Type.Mine;
                notifySubscribers("wholeFieldUpdate");
                notifySubscribers("lost");
                return;
            }
            data.playerField[x][y].type = FieldCellState.Type.Empty;
            emptyFieldDFS(x, y);
        }
        notifySubscribers("wholeFieldUpdate");
        if (isGameComplete()) {
            notifySubscribers("won");
        }
    }

    private void fillFields(int x, int y) {
        var data = getProperty();

        // Initial filling
        for (int i = 0; i < data.settings.getWidth(); i++) {
            for (int j = 0; j < data.settings.getHeight(); j++) {
                data.realField[i][j].type = FieldCellState.Type.Empty;
                data.playerField[i][j].type = FieldCellState.Type.Unknown;
            }
        }

        // Create indexes range
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < data.settings.getWidth() * data.settings.getHeight(); i++) {
            indexes.add(i);
        }

        indexes.remove((Integer) (y * data.settings.getWidth() + x));
        for (var neighbour : NEIGHBOURS) {
            int mx = x + neighbour[0];
            int my = y + neighbour[1];
            indexes.remove((Integer) (my * data.settings.getWidth() + mx));
        }

        // Shuffle and set mines
        Collections.shuffle(indexes);
        for (int i = 0; i < data.settings.getMinesCount(); i++) {
            int idx = indexes.get(i);
            data.realField[idx % data.settings.getWidth()][idx / data.settings.getWidth()].type = FieldCellState.Type.Mine;
        }

        // Calculate digits for player
        for (int i = 0; i < data.settings.getMinesCount(); i++) {
            int idx = indexes.get(i);
            for (var neighbour : NEIGHBOURS) {
                int mx = idx % data.settings.getWidth() + neighbour[0];
                int my = idx / data.settings.getWidth() + neighbour[1];
                if (checkCoordinates(mx, my)) {
                    data.playerField[mx][my].value++;
                }
            }
        }
    }

    private void emptyFieldDFS(int x, int y) {
        var data = getProperty();
        if (data.realField[x][y].type == FieldCellState.Type.Empty) {
            data.playerField[x][y].type = FieldCellState.Type.Empty;
            if (data.playerField[x][y].value == 0) {
                for (var neighbour : NEIGHBOURS) {
                    int mx = x + neighbour[0];
                    int my = y + neighbour[1];
                    if (checkCoordinates(mx, my) && data.playerField[mx][my].type == FieldCellState.Type.Unknown) {
                        emptyFieldDFS(mx, my);
                    }
                }
            }
        }
    }

    public void flag(int x, int y) throws InvalidArgumentException {
        checkCoordinatesWithException(x, y);
        var data = getProperty();
        if (data.playerField[x][y].type == FieldCellState.Type.Unknown) {
            data.playerField[x][y].type = FieldCellState.Type.Flag;
        } else if (data.playerField[x][y].type == FieldCellState.Type.Flag) {
            data.playerField[x][y].type = FieldCellState.Type.Unknown;
        }
        notifySubscribers("wholeFieldUpdate");
    }

    private boolean isGameComplete() {
        var data = getProperty();

        boolean won = true;

        for (int i = 0; i < data.settings.getWidth(); i++) {
            for (int j = 0; j < data.settings.getHeight(); j++) {
                if (data.realField[i][j].type == FieldCellState.Type.Empty && data.playerField[i][j].type != FieldCellState.Type.Empty) {
                    won = false;
                    break;
                }
            }
        }
        return won;
    }

    public FieldCellState[][] getPlayerField() {
        return getProperty().playerField;
    }

//    public FieldCellState[][] getRealField() {
//        return getProperty().realField;
//    }

    public int getWidth() {
        return getProperty().settings.getWidth();
    }

    public int getHeight() {
        return getProperty().settings.getHeight();
    }

    public int getMineCount() {
        return getProperty().settings.getMinesCount();
    }
}
