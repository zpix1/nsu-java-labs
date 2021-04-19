package ru.nsu.fit.ibaksheev.minesweeper.model;

import lombok.Getter;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GameModel extends Model<GameData> {
    private static final int[][] NEIGHBOURS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    @Getter
    private int updatedFieldCellX = 0;
    @Getter
    private int updatedFieldCellY = 0;

    public GameModel() {
        super(null);
    }

    public void setGameData(GameData data) {
        this.setProperty(data);
        notifySubscribers("reset");
    }

    public void pregenerate() {
        fillFields(0, 0);
        getProperty().setFirstShotDone(true);
    }

    public void shoot(int x, int y) throws InvalidArgumentException {
        checkCoordinatesWithException(x, y);
        var data = getProperty();
        if (data.getPlayerField()[x][y].getType() == FieldCellState.Type.Flag) {
            return;
        }
        if (!data.isFirstShotDone()) {
            data.setState(GameData.State.STARTED);
            data.setStartedAt(new Date());

            fillFields(x, y);
            data.getPlayerField()[x][y].setType(FieldCellState.Type.Empty);
            emptyFieldDFS(x, y);
            data.setFirstShotDone(true);
        } else {
            if (data.getPlayerField()[x][y].getType() == FieldCellState.Type.Flag) {
                return;
            }
            if (data.getRealField()[x][y].getType() == FieldCellState.Type.Mine) {
                data.getPlayerField()[x][y].setType(FieldCellState.Type.Mine);
                data.setState(GameData.State.LOST);
                showMinesForUser();
                notifySubscribers("lost");
                return;
            }
            data.getPlayerField()[x][y].setType(FieldCellState.Type.Empty);
            emptyFieldDFS(x, y);
        }
        if (isGameComplete()) {
            data.setState(GameData.State.WON);
            notifySubscribers("won");
        }
    }

    private void updateCell(int x, int y) {
        updatedFieldCellX = x;
        updatedFieldCellY = y;
        notifySubscribers("fieldCellUpdate");
    }

    private void showMinesForUser() {
        var data = getProperty();

        for (int i = 0; i < data.getSettings().getWidth(); i++) {
            for (int j = 0; j < data.getSettings().getHeight(); j++) {
                if (data.getRealField()[i][j].getType() == FieldCellState.Type.Mine) {
                    data.getPlayerField()[i][j].setType(FieldCellState.Type.Mine);
                    updateCell(i, j);
                }
            }
        }
        notifySubscribers("fieldUpdate");
    }

    private void fillFields(int x, int y) {
        var data = getProperty();

        // Initial filling
        for (int i = 0; i < data.getSettings().getWidth(); i++) {
            for (int j = 0; j < data.getSettings().getHeight(); j++) {
                data.getRealField()[i][j].setType(FieldCellState.Type.Empty);
                data.getPlayerField()[i][j].setType(FieldCellState.Type.Unknown);
            }
        }

        // Create indexes range
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < data.getSettings().getWidth() * data.getSettings().getHeight(); i++) {
            indexes.add(i);
        }

        indexes.remove((Integer) (y * data.getSettings().getWidth() + x));
        for (var neighbour : NEIGHBOURS) {
            int mx = x + neighbour[0];
            int my = y + neighbour[1];
            indexes.remove((Integer) (my * data.getSettings().getWidth() + mx));
        }

        // Shuffle and set mines
        Collections.shuffle(indexes);
        for (int i = 0; i < data.getSettings().getMinesCount(); i++) {
            int idx = indexes.get(i);
            data.getRealField()[idx % data.getSettings().getWidth()][idx / data.getSettings().getWidth()].setType(FieldCellState.Type.Mine);
        }

        // Calculate digits for player
        for (int i = 0; i < data.getSettings().getMinesCount(); i++) {
            int idx = indexes.get(i);
            for (var neighbour : NEIGHBOURS) {
                int mx = idx % data.getSettings().getWidth() + neighbour[0];
                int my = idx / data.getSettings().getWidth() + neighbour[1];
                if (checkCoordinates(mx, my)) {
                    data.getPlayerField()[mx][my].setValue(data.getPlayerField()[mx][my].getValue() + 1);
                }
            }
        }
    }

    // Returns how many field it has updated
    private int emptyFieldDFS(int x, int y) {
        int value = _emptyFieldDFS(x, y);

        notifySubscribers("fieldUpdate");

        return value;
    }

    private int _emptyFieldDFS(int x, int y) {
        var data = getProperty();
        int fieldsUpdated = 0;
        if (data.getRealField()[x][y].getType() == FieldCellState.Type.Empty) {
            fieldsUpdated += 1;
            data.getPlayerField()[x][y].setType(FieldCellState.Type.Empty);
            updateCell(x, y);
            if (data.getPlayerField()[x][y].getValue() == 0) {
                for (var neighbour : NEIGHBOURS) {
                    int mx = x + neighbour[0];
                    int my = y + neighbour[1];
                    if (checkCoordinates(mx, my) && data.getPlayerField()[mx][my].getType() == FieldCellState.Type.Unknown) {
                        fieldsUpdated += _emptyFieldDFS(mx, my);
                    }
                }
            }
        }
        return fieldsUpdated;
    }

    public void flag(int x, int y) throws InvalidArgumentException {
        checkCoordinatesWithException(x, y);
        var data = getProperty();
        if (data.getPlayerField()[x][y].getType() == FieldCellState.Type.Unknown) {
            data.getPlayerField()[x][y].setType(FieldCellState.Type.Flag);
            System.out.println("set to flag");

            updatedFieldCellX = x;
            updatedFieldCellY = y;
        } else if (data.getPlayerField()[x][y].getType() == FieldCellState.Type.Flag) {
            data.getPlayerField()[x][y].setType(FieldCellState.Type.Unknown);
            System.out.println("set to unknown");


            updatedFieldCellX = x;
            updatedFieldCellY = y;
        }
        notifySubscribers("fieldCellUpdate");
        notifySubscribers("fieldUpdate");
    }

    private void checkCoordinatesWithException(int x, int y) throws InvalidArgumentException {
        if (checkCoordinates(x, y)) {
            return;
        }
        throw new InvalidArgumentException("invalid coordinates: " + x + " " + y);
    }

    private boolean checkCoordinates(int x, int y) {
        var data = getProperty();
        return 0 <= x && x < data.getSettings().getWidth() && 0 <= y && y < data.getSettings().getHeight();
    }

    private boolean isGameComplete() {
        var data = getProperty();

        boolean won = true;

        for (int i = 0; i < data.getSettings().getWidth(); i++) {
            for (int j = 0; j < data.getSettings().getHeight(); j++) {
                if (data.getRealField()[i][j].getType() == FieldCellState.Type.Empty && data.getPlayerField()[i][j].getType() != FieldCellState.Type.Empty) {
                    won = false;
                    break;
                }
            }
        }
        return won;
    }

    public FieldCellState[][] getPlayerField() {
        return getProperty().getPlayerField();
    }

    public Date getStartedAt() {
        return getProperty().getStartedAt();
    }

    public GameData.State getState() {
        return getProperty().getState();
    }
}
