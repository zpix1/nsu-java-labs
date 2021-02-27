package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import ru.nsu.fit.ibaksheev.minesweeper.controller.GameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;
import ru.nsu.fit.ibaksheev.minesweeper.view.GameView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameGUIView extends GameView {

    private JFrame window;

    GameData.Settings defaultSettings = new GameData.Settings(9, 9, 10);

    public GameGUIView(GameModel model, GameController controller) {
        super(model, controller);
    }

    MouseAdapter adapter = new MouseAdapter() {
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

    private JMenu createGameMenu() {
        var game = new JMenu("Game");
        var exit = new JMenuItem("Exit");
        exit.addActionListener(event -> exit());

        var scores = new JMenuItem("Scores");
        scores.addActionListener(event -> showScores());

        var about = new JMenuItem("About");
        about.addActionListener(event -> about());

        var newGame = new JMenu("New game");
        for (var entry : model.settingsManager.getSettingsList()) {
            var settings = entry.getValue();
            var settingMenu = new JMenuItem(settings.getName());
            settingMenu.addActionListener(event -> {
                defaultSettings = settings;
                newGameWithSettings();
            });
            newGame.add(settingMenu);
        }
        game.add(newGame);
        game.add(scores);
        game.add(about);
        game.add(exit);
        return game;
    }

    @Override
    public void start() {
        window = new JFrame("Minesweeper");
        JFrame.setDefaultLookAndFeelDecorated(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        var menuBar = new JMenuBar();
        menuBar.add(createGameMenu());
        window.setJMenuBar(menuBar);

        var field = new GUIField();

        newGameWithSettings();

        model.subscribe(model -> lost(), "lost");

        model.subscribe(model -> won(), "won");

        model.subscribe(model -> field.updateField(this.model.getPlayerField()), "wholeFieldUpdate");

        model.subscribe(model -> field.updateFieldCell(this.model.getPlayerField(), this.model.getUpdatedFieldCellX(), this.model.getUpdatedFieldCellY()), "fieldCellUpdate");

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
        window.setSize(defaultSettings.getHeight() * GUIFieldCell.CELL_SIZE, defaultSettings.getWidth() * GUIFieldCell.CELL_SIZE);
        controller.newGame(defaultSettings);
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
        JOptionPane.showInternalMessageDialog(null, "Todo...",
                "Scores", JOptionPane.INFORMATION_MESSAGE);
    }
    private void about() {
        JOptionPane.showInternalMessageDialog(null, "Minesweeper v0.1\nMade by Ivan Baksheev <zpix-dev@list.ru>",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }


    private void exit() {
        System.exit(0);
    }
}
