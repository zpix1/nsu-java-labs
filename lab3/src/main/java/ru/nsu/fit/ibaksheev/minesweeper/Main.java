package ru.nsu.fit.ibaksheev.minesweeper;

import ru.nsu.fit.ibaksheev.minesweeper.controller.GameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.view.View;
import ru.nsu.fit.ibaksheev.minesweeper.view.gui.GameGUIView;
import ru.nsu.fit.ibaksheev.minesweeper.view.text.GameTextView;

public class Main {
    public static void main(String[] args) {
        var model = new GameModel();
        var controller = new GameController(model);
        View<GameData> view = new GameGUIView(model, controller);

        view.start();
    }
}
