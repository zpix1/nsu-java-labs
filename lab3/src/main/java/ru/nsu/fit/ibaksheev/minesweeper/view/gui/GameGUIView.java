package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import org.joda.time.Duration;
import ru.nsu.fit.ibaksheev.minesweeper.Utils;
import ru.nsu.fit.ibaksheev.minesweeper.controller.GameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.SettingsManager;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;
import ru.nsu.fit.ibaksheev.minesweeper.view.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

public class GameGUIView extends GameView {

    SettingsManager settingsManager = new SettingsManager();
    SettingsManager.Settings defaultSettings = settingsManager.getFirstSettings();
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
    private JFrame window;
    private JLabel timerField;
    private GUIField field;

    public GameGUIView(GameModel model, GameController controller) {
        super(model, controller);
    }

    private JMenu createGameMenu() {
        var game = new JMenu("Game");
        var exit = new JMenuItem("Exit");
        exit.addActionListener(event -> exit());

        var scores = new JMenuItem("Scores");
        scores.addActionListener(event -> showScores());

        var about = new JMenuItem("About");
        about.addActionListener(event -> about());

        var newGame = new JMenu("New game");
        for (var entry : settingsManager.getSettingsList()) {
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
        SwingUtilities.invokeLater(this::startGUI);
    }

    public void startGUI() {
        window = new JFrame("Minesweeper");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();
            }
        });
        window.setResizable(false);
        window.setLayout(new BorderLayout());

        var menuBar = new JMenuBar();
        menuBar.add(createGameMenu());
        window.setJMenuBar(menuBar);

        timerField = new JLabel("");
        timerField.setHorizontalAlignment(SwingConstants.CENTER);
        timerField.setVerticalAlignment(SwingConstants.CENTER);
        window.add(timerField, BorderLayout.NORTH);

        // TODO: Where to create timer thread?
        var timerThread = new Thread(() -> {
            while (true) {
                if (model.propertyExist() && model.getState() == GameData.State.STARTED) {
//                    https://stackoverflow.com/questions/1555262/calculating-the-difference-between-two-java-date-instances
                    var duration = new Duration(model.getStartedAt().getTime(), new Date().getTime());
                    timerField.setText(Utils.formatDuration(duration));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        timerThread.start();

        field = new GUIField();

        controller.loadOrStartNew(defaultSettings);

        model.subscribe(model -> lost(), "lost");

        model.subscribe(model -> won(), "won");

        model.subscribe(model -> field.updateField(this.model.getPlayerField()), "wholeFieldUpdate");

        model.subscribe(model -> field.updateFieldCell(this.model.getPlayerField(), this.model.getUpdatedFieldCellX(), this.model.getUpdatedFieldCellY()), "fieldCellUpdate");

        model.subscribe(model -> {
            field.setField(this.model.getPlayerField(), adapter);
            window.setVisible(true);
        }, "reset");

        field.setField(model.getPlayerField(), adapter);

        resize();
        window.add(field);
        window.setVisible(true);
    }

    private void dispose() {
        controller.dispose();
    }

    private void resize() {
        // TODO: Why setSize get ints, but getWidth/getHeight methods return doubles?
        // TODO: How to make window adapt to size of its components?
        timerField.setPreferredSize(new Dimension(20, 20));
        window.setSize(new Dimension(field.getWidth(), (int) (field.getHeight() + timerField.getPreferredSize().getHeight())));
    }

    private void newGameWithSettings() {
        controller.newGameWithSettings(defaultSettings);
        resize();
        timerField.setText("");
    }

    private void lost() {
        JOptionPane.showInternalMessageDialog(null,
                "I am sorry, but you just exploded!", "You lost...", JOptionPane.INFORMATION_MESSAGE);
        controller.endGame();
        newGameWithSettings();
    }

    private void won() {
        JOptionPane.showInternalMessageDialog(null, "You won!",
                "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        controller.endGame();
        newGameWithSettings();
    }

    private void showScores() {
        var lastEntries = model.getScoresManager().getEntries();
        var header = new Vector<>(Arrays.asList("Difficulty", "Time"));
        var data = new Vector<Vector<String>>();
        for (var entry : lastEntries) {
            var row = new Vector<>(Arrays.asList(entry.getSettingsName(), entry.getDurationString()));
            data.add(row);
        }

        var table = new JTable(data, header);
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);

        var dialog = new JFrame("Scores");
        dialog.setSize(500, 500);
        dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        dialog.setResizable(false);

        dialog.setContentPane(new JScrollPane(table));

        dialog.setVisible(true);
    }

    private void about() {
        JOptionPane.showInternalMessageDialog(null, "Minesweeper v0.1\nMade by Ivan Baksheev <zpix-dev@list.ru>",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }


    private void exit() {
        dispose();
        System.exit(0);
    }
}
