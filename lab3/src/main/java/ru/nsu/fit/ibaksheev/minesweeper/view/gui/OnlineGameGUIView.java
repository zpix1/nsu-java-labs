package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import ru.nsu.fit.ibaksheev.minesweeper.controller.OnlineGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;
import ru.nsu.fit.ibaksheev.minesweeper.view.GameView;
import ru.nsu.fit.ibaksheev.minesweeper.view.View;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OnlineGameGUIView implements GameView {
    private final GameModel syncModel;
    private final OnlineGameController controller;
    private final GameModel model;

    private final MouseAdapter adapter = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            var cell = (GUIFieldCell) e.getSource();
            if (e.getButton() == MouseEvent.BUTTON3) {
                try {
                    controller.flag(cell.getFieldX(), cell.getFieldY());
                } catch (InvalidArgumentException invalidArgumentException) {
                    invalidArgumentException.printStackTrace();
                }
            } else if (e.getButton() == MouseEvent.BUTTON1) {
                try {
                    controller.shoot(cell.getFieldX(), cell.getFieldY());
                } catch (InvalidArgumentException invalidArgumentException) {
                    invalidArgumentException.printStackTrace();
                }
            }
        }
    };

    public OnlineGameGUIView(OnlineGameController controller) {
        this.controller = controller;
        model = controller.getModel();
        syncModel = controller.getSyncModel();
    }

    @Override
    public void start() {

    }
}
