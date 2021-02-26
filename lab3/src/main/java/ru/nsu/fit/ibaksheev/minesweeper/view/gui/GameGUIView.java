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

    Settings defaultSettings = new Settings(10, 5, 1);

    public GameGUIView(GameModel model, GameController controller) {
        super(model, controller);
    }

    MouseAdapter adapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            var cell = (GUIFieldCell) e.getSource();
            System.out.println("Clicked: "+ cell.getFieldX() + " " + cell.getFieldY());
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
        var exit = new JMenuItem("Exit");
        exit.addActionListener(event -> exit());

        var scores = new JMenuItem("Scores");
        scores.addActionListener(event -> showScores());

        var newGame = new JMenu("New game");
        var easy = new JMenuItem("Easy");
        easy.addActionListener(event -> {
            defaultSettings.width = 9;
            defaultSettings.height = 9;
            defaultSettings.minesCount = 10;
            newGameWithSettings();
        });
        var medium = new JMenuItem("Medium");
        medium.addActionListener(event -> {
            defaultSettings.width = 20;
            defaultSettings.height = 10;
            defaultSettings.minesCount = 22;
            newGameWithSettings();
        });
        var hard = new JMenuItem("Hard");
        hard.addActionListener(event -> {
            defaultSettings.width = 30;
            defaultSettings.height = 20;
            defaultSettings.minesCount = 80;
            newGameWithSettings();
        });
        newGame.add(easy); newGame.add(medium); newGame.add(hard);
        game.add(newGame);
        game.add(scores);
        game.add(exit);
        return game;
    }

    @Override
    public void start() {
        newGameWithSettings();

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


    private void showScores() {
        JOptionPane.showInternalMessageDialog(null, "Scores",
                "TODO", JOptionPane.INFORMATION_MESSAGE);
    }


    private void exit() {
        System.exit(0);
    }
}
