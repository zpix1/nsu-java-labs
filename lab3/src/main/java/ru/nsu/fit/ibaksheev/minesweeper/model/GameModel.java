package ru.nsu.fit.ibaksheev.minesweeper.model;

import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.GameException;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

public class GameModel extends Model<GameData> {
    public GameModel() {
        super(null);
    }

    public void setGameData(GameData data) {
        this.setProperty(data);
        notifySubscribers("wholeFieldUpdate");
    }

    private void checkCoordinates(int x, int y) throws InvalidArgumentException {
        var data = getProperty();
        if (0 <= x && x < data.width && 0 <= y && y < data.height) {
            return;
        }
        throw new InvalidArgumentException("invalid coordinates: " + x + " " + y);
    }

    public void shot(int x, int y) throws InvalidArgumentException {
        checkCoordinates(x, y);
        var data = getProperty();
        data.realField[x][y].type = FieldCellState.Type.Empty;
        data.playerField[x][y].type = FieldCellState.Type.Empty;
//        if (!data.firstShotDone) {
//            // TODO: Generate field
//
//            notifySubscribers("wholeFieldUpdate");
//        } else {
//            // TODO: Produce shot effects
//
//            notifySubscribers("wholeFieldUpdate");
//        }
    }

    public void flag(int x, int y) throws InvalidArgumentException {
        checkCoordinates(x, y);
        var data = getProperty();
        if (data.playerField[x][y].type == FieldCellState.Type.Unknown) {
            data.playerField[x][y].type = FieldCellState.Type.Flag;
        } else if (data.playerField[x][y].type == FieldCellState.Type.Flag) {
            data.playerField[x][y].type = FieldCellState.Type.Unknown;
        }
    }

    public FieldCellState[][] getPlayerField() {
        return getProperty().playerField;
    }

    public int getWidth() {
        return getProperty().width;
    }

    public int getHeight() {
        return getProperty().height;
    }
}
