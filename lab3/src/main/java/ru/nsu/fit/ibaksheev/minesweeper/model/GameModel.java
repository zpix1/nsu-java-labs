package ru.nsu.fit.ibaksheev.minesweeper.model;

import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GameModel extends Model<GameData> {

    private static final int[][] NEIGHBOURS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    private int updatedFieldCellX = 0;
    private int updatedFieldCellY = 0;

    private final ScoresManager scoresManager;

    public GameModel() {
        super(null);
        scoresManager = new ScoresManager();
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
            data.state = GameData.State.STARTED;
            data.startedAt = new Date();

            fillFields(x, y);
            data.playerField[x][y].type = FieldCellState.Type.Empty;
            emptyFieldDFS(x, y);
            data.firstShotDone = true;
            notifySubscribers("wholeFieldUpdate");
        } else {
            if (data.realField[x][y].type == FieldCellState.Type.Mine) {
                data.playerField[x][y].type = FieldCellState.Type.Mine;
                data.state = GameData.State.LOST;
                showMinesForUser();
                notifySubscribers("wholeFieldUpdate");
                notifySubscribers("lost");
                return;
            }
            data.playerField[x][y].type = FieldCellState.Type.Empty;
            int res = emptyFieldDFS(x, y);
            if (res != 1) {
                notifySubscribers("wholeFieldUpdate");
            } else {
                notifySubscribers("fieldCellUpdate");
            }
        }
        if (isGameComplete()) {
            data.state = GameData.State.WON;
            notifySubscribers("won");
        }
    }

    private void showMinesForUser() {
        var data = getProperty();

        for (int i = 0; i < data.settings.getWidth(); i++) {
            for (int j = 0; j < data.settings.getHeight(); j++) {
                if (data.realField[i][j].type == FieldCellState.Type.Mine) {
                    data.playerField[i][j].type = FieldCellState.Type.Mine;
                }
            }
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

    // returns how many field it has updated
    private int emptyFieldDFS(int x, int y) {
        var data = getProperty();
        int fieldsUpdated = 0;
        if (data.realField[x][y].type == FieldCellState.Type.Empty) {
            updatedFieldCellX = x;
            updatedFieldCellY = y;
            fieldsUpdated += 1;
            data.playerField[x][y].type = FieldCellState.Type.Empty;
            if (data.playerField[x][y].value == 0) {
                for (var neighbour : NEIGHBOURS) {
                    int mx = x + neighbour[0];
                    int my = y + neighbour[1];
                    if (checkCoordinates(mx, my) && data.playerField[mx][my].type == FieldCellState.Type.Unknown) {
                        fieldsUpdated += emptyFieldDFS(mx, my);
                    }
                }
            }
        }
        return fieldsUpdated;
    }

    public void flag(int x, int y) throws InvalidArgumentException {
        checkCoordinatesWithException(x, y);
        var data = getProperty();
        if (data.playerField[x][y].type == FieldCellState.Type.Unknown) {
            data.playerField[x][y].type = FieldCellState.Type.Flag;

            updatedFieldCellX = x;
            updatedFieldCellY = y;
        } else if (data.playerField[x][y].type == FieldCellState.Type.Flag) {
            data.playerField[x][y].type = FieldCellState.Type.Unknown;

            updatedFieldCellX = x;
            updatedFieldCellY = y;
        }
        notifySubscribers("fieldCellUpdate");
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

    public Date getStartedAt() {
        return getProperty().startedAt;
    }

    public GameData.State getState() {
        return getProperty().state;
    }

    public SettingsManager.Settings getSettings() {
        return getProperty().settings;
    }

    public int getUpdatedFieldCellX() {
        return updatedFieldCellX;
    }

    public int getUpdatedFieldCellY() {
        return updatedFieldCellY;
    }

    public ScoresManager getScoresManager() {
        return scoresManager;
    }
}
