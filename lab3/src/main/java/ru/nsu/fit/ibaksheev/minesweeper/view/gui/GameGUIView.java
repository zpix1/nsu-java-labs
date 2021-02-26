package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import ru.nsu.fit.ibaksheev.minesweeper.controller.GameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.FieldCellState;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;
import ru.nsu.fit.ibaksheev.minesweeper.view.GameView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class GameGUIView extends GameView {

    private JFrame window;

    static class Settings {
        public int width, height, minesCount;

        public Settings(int width, int height, int minesCount) {
            this.width = width;
            this.height = height;
            this.minesCount = minesCount;
        }
    }

    Settings defaultSettings = new Settings(9, 9, 10);

    public GameGUIView(GameModel model, GameController controller) {
        super(model, controller);
    }

    MouseAdapter adapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
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

    private JMenu createGameMenu() {
        var game = new JMenu("Game");
        var newGame = new JMenu("New game");
        var easy = new JMenuItem("Easy");
        easy.setName("new-game-easy");
        var medium = new JMenuItem("Medium");
        medium.setName("new-game-medium");
        var hard = new JMenuItem("Hard");
        hard.setName("new-game-hard");
        newGame.add(easy); newGame.add(medium); newGame.add(hard);
        game.add(newGame);
        return game;
    }

    @Override
    public void start() {
        controller.newGame(9, 9, 10);


        window = new JFrame("Minesweeper");
        JFrame.setDefaultLookAndFeelDecorated(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 500);

        var menuBar = new JMenuBar();
        menuBar.add(createGameMenu());
        window.setJMenuBar(menuBar);

        var field = new GUIField();

        model.subscribe(model -> lost(), "lost");

        model.subscribe(model -> won(), "won");

        model.subscribe(model -> field.updateField(this.model.getPlayerField()), "wholeFieldUpdate");

        model.subscribe(model -> {
            System.out.println(this.model.getPlayerField().length);
            field.setField(this.model.getPlayerField(), adapter);
            window.setVisible(true);
        }, "reset");


        field.setField(model.getPlayerField(), adapter);

        window.add(field);
        window.setVisible(true);
    }

    private void newGameWithSettings() {
        controller.newGame(defaultSettings.width, defaultSettings.height, defaultSettings.minesCount);
    }

    private void lost() {
        JOptionPane.showInternalMessageDialog(null,
                "I am sorry, but you just exploded!", "You lost...", JOptionPane.INFORMATION_MESSAGE);
        newGameWithSettings();
    }

    private void won() {
        JOptionPane.showInternalMessageDialog(null, "You won!",
                "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        newGameWithSettings();
    }

}
