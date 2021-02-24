package ru.nsu.fit.ibaksheev.minesweeper;

public class GameModel extends Model<GameData> {
    public GameModel(int x, int y, int mineCount) {
        super(new GameData(x, y, mineCount));
    }

    void shot(int x, int y) throws GameException {
        var data = getProperty();
        if (0 <= x && x < data.width && 0 <= y && y < data.height) {
            if (!data.firstShotDone) {
                // TODO: Generate field

                notifySubscribers("wholeFieldUpdate");
            } else {
                // TODO: Produce shot effects

                notifySubscribers("wholeFieldUpdate");
            }
        } else {
            throw new GameException("invalid coordinates: " + x + " " + y);
        }
    }
}
