package ru.nsu.fit.ibaksheev.minesweeper;

public class Controller {
    GameModel currentModel;

    void shot(int x, int y) {
        try {
            currentModel.shot(x, y);
        } catch (GameException e) {
            // TODO: add logging
            e.printStackTrace();
        }
    }
}
