package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import lombok.Data;
import org.joda.time.Duration;
import ru.nsu.fit.ibaksheev.minesweeper.Utils;
import ru.nsu.fit.ibaksheev.minesweeper.controller.LocalGameController;
import ru.nsu.fit.ibaksheev.minesweeper.controller.OnlineGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.SettingsGameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;
import ru.nsu.fit.ibaksheev.minesweeper.view.GameView;
import ru.nsu.fit.ibaksheev.minesweeper.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class OnlineGameGUIView implements GameView {
    private final GameModel syncModel;
    private final OnlineGameController controller;
    private final GameModel model;


    private static final int ANIMATION_STEP = 0;
    private JFrame window;
    private GUIField field;
    private GUIField syncField;

    private final BlockingQueue<OnlineGameGUIView.AnimationCell> animationQueue = new LinkedBlockingDeque<>();

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

    private final MouseAdapter syncAdapter = new MouseAdapter() {};

    void won() {
        System.out.println("won!");
    }

    void lost() {
        System.out.println("lost!");
    }

    public void startGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        window = new JFrame("Minesweeper Online");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setResizable(false);
        window.setLayout(new BorderLayout(5, 5));

        new Timer(ANIMATION_STEP, e -> {
            var elem = animationQueue.poll();
            if (elem != null) {
                field.updateFieldCell(this.model.getPlayerField(), elem.getX(), elem.getY());
            }
        }).start();

        var fieldPanel = new Panel(new BorderLayout());
        fieldPanel.add(new JLabel("Your field", SwingConstants.CENTER), BorderLayout.NORTH);

        field = new GUIField();
        model.subscribe(model -> lost(), "lost");
        model.subscribe(model -> won(), "won");
        model.subscribe(model -> animationQueue.add(new OnlineGameGUIView.AnimationCell(this.model.getUpdatedFieldCellX(), this.model.getUpdatedFieldCellY())), "fieldCellUpdate");
        field.setField(model.getPlayerField(), adapter);
        fieldPanel.add(field, BorderLayout.SOUTH);

        window.add(fieldPanel, BorderLayout.WEST);

        var syncFieldPanel = new Panel(new BorderLayout());
        syncFieldPanel.add(new JLabel("Opponent field", SwingConstants.CENTER), BorderLayout.NORTH);

        syncField = new GUIField();
        syncField.setField(syncModel.getPlayerField(), syncAdapter);
        syncFieldPanel.add(syncField, BorderLayout.SOUTH);

        window.add(syncFieldPanel, BorderLayout.EAST);

        window.pack();

        window.setVisible(true);
    }

    public OnlineGameGUIView(OnlineGameController controller) {
        this.controller = controller;
        model = controller.getModel();
        syncModel = controller.getSyncModel();
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(this::startGUI);
    }

    @Data
    private static class AnimationCell {
        private final int x, y;
    }
}
