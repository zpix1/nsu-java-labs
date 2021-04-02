package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import lombok.Data;
import org.joda.time.Duration;
import ru.nsu.fit.ibaksheev.minesweeper.Utils;
import ru.nsu.fit.ibaksheev.minesweeper.controller.LocalGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.SettingsGameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.SettingsManager;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;
import ru.nsu.fit.ibaksheev.minesweeper.view.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class GameGUIView extends GameView {
    private static final int ANIMATION_STEP = 0;
    private final SettingsManager settingsManager = new SettingsManager();
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
    private final BlockingQueue<AnimationCell> animationQueue = new LinkedBlockingDeque<>();
    private SettingsManager.Settings defaultSettings = settingsManager.getFirstSettings();
    private JFrame window;
    private JLabel timerField;
    private GUIField field;

    public GameGUIView(SettingsGameModel model, LocalGameController controller) {
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
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        new Timer(1000, e -> {
            if (model.propertyExists() && model.getState() == GameData.State.STARTED) {
//                    https://stackoverflow.com/questions/1555262/calculating-the-difference-between-two-java-date-instances
                var duration = new Duration(model.getStartedAt().getTime(), new Date().getTime());
                timerField.setText(Utils.formatDuration(duration));
            }
        }).start();

        new Timer(ANIMATION_STEP, e -> {
            var elem = animationQueue.poll();
            if (elem != null) {
                field.updateFieldCell(this.model.getPlayerField(), elem.getX(), elem.getY());
            }
        }).start();


        field = new GUIField();

        controller.loadOrStartNew(defaultSettings);

        model.subscribe(model -> lost(), "lost");

        model.subscribe(model -> won(), "won");

        model.subscribe(model -> animationQueue.add(new AnimationCell(this.model.getUpdatedFieldCellX(), this.model.getUpdatedFieldCellY())), "fieldCellUpdate");

        model.subscribe(model -> {
            field.setField(this.model.getPlayerField(), adapter);
            window.setVisible(true);
        }, "reset");

        field.setField(model.getPlayerField(), adapter);

        window.add(field);
        resize();
        window.setVisible(true);
    }

    private void dispose() {
        controller.dispose();
    }

    private void resize() {
        // TODO: Why setSize get ints, but getWidth/getHeight methods return doubles?
        timerField.setPreferredSize(new Dimension(20, 20));
        window.pack();
    }

    private void newGameWithSettings() {
        controller.newGameWithSettings(defaultSettings);
        resize();
        timerField.setText("");
    }

    private void lost() {
        controller.endGame();
        JOptionPane.showInternalMessageDialog(null,
                "I am sorry, but you just exploded!", "You lost...", JOptionPane.INFORMATION_MESSAGE);
        newGameWithSettings();
    }

    private void won() {
        controller.endGame();
        JOptionPane.showInternalMessageDialog(null, "You won!",
                "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
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

    @Data
    private static class AnimationCell {
        private final int x, y;
    }
}
