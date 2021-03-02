package ru.nsu.fit.ibaksheev.minesweeper;

import ru.nsu.fit.ibaksheev.minesweeper.controller.GameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.view.View;
import ru.nsu.fit.ibaksheev.minesweeper.view.gui.GameGUIView;
import ru.nsu.fit.ibaksheev.minesweeper.view.text.GameTextView;

public class Main {
    public static void main(String[] args) {
        var controller = new GameController();
        var model = controller.getModel();
        View<GameData> view = new GameGUIView(model, controller);

        view.start();
    }
}
