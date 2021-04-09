package ru.nsu.fit.ibaksheev.minesweeper;

import ru.nsu.fit.ibaksheev.minesweeper.controller.LocalGameController;
import ru.nsu.fit.ibaksheev.minesweeper.controller.OnlineGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.view.View;
import ru.nsu.fit.ibaksheev.minesweeper.view.gui.GameGUIView;
import ru.nsu.fit.ibaksheev.minesweeper.view.gui.OnlineGameGUIView;

public class Main {
    public static void main(String[] args) {
//        var controller = new LocalGameController();
//        View<GameData> view = new GameGUIView(controller);
//
//        view.start();

        var controller = new OnlineGameController();
        View<GameData> view = new OnlineGameGUIView(controller);
        view.start();
    }
}
