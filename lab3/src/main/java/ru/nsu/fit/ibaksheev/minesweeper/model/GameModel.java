package ru.nsu.fit.ibaksheev.minesweeper.model;

import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.GameException;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

public class GameModel extends Model<GameData> {
    public GameModel(int x, int y, int mineCount) {
        super(new GameData(x, y, mineCount));
    }

    public void shot(int x, int y) throws InvalidArgumentException {
        var data = getProperty();
        if (0 <= x && x < data.width && 0 <= y && y < data.height) {
            data.realField[x][y].type = FieldCellState.Type.Empty;
            data.playerField[x][y].type = FieldCellState.Type.Empty;
//            if (!data.firstShotDone) {
//                // TODO: Generate field
//
//                notifySubscribers("wholeFieldUpdate");
//            } else {
//                // TODO: Produce shot effects
//
//                notifySubscribers("wholeFieldUpdate");
//            }
        } else {
            throw new InvalidArgumentException("invalid coordinates: " + x + " " + y);
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
