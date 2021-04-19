package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.time.Duration;
import ru.nsu.fit.ibaksheev.minesweeper.Utils;
import ru.nsu.fit.ibaksheev.minesweeper.controller.LocalGameController;
import ru.nsu.fit.ibaksheev.minesweeper.controller.OnlineGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.*;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;
import ru.nsu.fit.ibaksheev.minesweeper.view.GameView;
import ru.nsu.fit.ibaksheev.minesweeper.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class OnlineGameGUIView implements GameView {
    private final GameModel syncModel;
    private final GameModel model;
    private final Model<OnlineGameController.NetworkState> networkModel;
    private final OnlineGameController controller;


    private static final int ANIMATION_STEP = 0;
    private JFrame window;
    private GUIField field;
    private GUIField syncField;
    private JLabel label;

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

    private void won() {
        System.out.println("won!");
    }

    private void lost() {
        System.out.println("lost!");
    }

    private void afterDisconnection(){
        window.removeAll();

        var loadingLabel = new JLabel("Disconnected...");
        window.add(loadingLabel, BorderLayout.CENTER);

        window.pack();
    }

    private void afterConnection() {
        window.setVisible(false);

        new Timer(ANIMATION_STEP, e -> {
            var elem = animationQueue.poll();
            if (elem != null) {
                elem.getField().updateFieldCell(elem.getModel().getPlayerField(), elem.getX(), elem.getY());
            }
        }).start();

        var fieldPanel = new Panel(new BorderLayout());
        fieldPanel.add(new JLabel("Your field", SwingConstants.CENTER), BorderLayout.NORTH);
        field = new GUIField();
        model.subscribe(model -> lost(), "lost");
        model.subscribe(model -> won(), "won");

        model.subscribe(temp -> {
            animationQueue.add(new AnimationCell(field, model, model.getUpdatedFieldCellX(), model.getUpdatedFieldCellY()));
        }, "fieldCellUpdate");

        syncModel.subscribe(temp -> {
            animationQueue.add(new AnimationCell(syncField, syncModel, syncModel.getUpdatedFieldCellX(), syncModel.getUpdatedFieldCellY()));
        }, "fieldCellUpdate");

        field.setField(model.getPlayerField(), adapter);

        var syncFieldPanel = new Panel(new BorderLayout());
        syncFieldPanel.add(new JLabel("Opponent field", SwingConstants.CENTER), BorderLayout.NORTH);
        syncField = new GUIField();
        syncField.setField(syncModel.getPlayerField(), syncAdapter);
        window.remove(label);

        syncFieldPanel.add(syncField, BorderLayout.SOUTH);
        fieldPanel.add(field, BorderLayout.SOUTH);
        window.add(syncFieldPanel, BorderLayout.EAST);
        window.add(fieldPanel, BorderLayout.WEST);


        window.pack();

        window.setVisible(true);
    }

    public void waitConnection() {
        label.setText("waiting for player...");
    }
    public void errorConnection() {
        label.setText("error... please restart application");
    }

    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        window = new JFrame("Minesweeper Online");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setResizable(false);
        window.setLayout(new BorderLayout(5, 5));

        label = new JLabel("Connecting...");
        window.add(label, BorderLayout.CENTER);
        window.pack();
        window.setVisible(true);
        window.createBufferStrategy(2);
    }

    public OnlineGameGUIView(OnlineGameController controller) {
        this.controller = controller;
        model = controller.getModel();
        syncModel = controller.getSyncModel();
        networkModel = controller.getNetworkModel();
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(this::init);
        networkModel.subscribe(model -> {
            System.out.println("updateProp");
            var newState = networkModel.getProperty();
            switch (newState) {
                case CONNECTED:
                    System.out.println("CONNECTED");
                    SwingUtilities.invokeLater(this::afterConnection);
                    break;
                case WAITING_FOR_PLAYER:
                    SwingUtilities.invokeLater(this::waitConnection);
                    break;
                case DISCONNECTED:
                case ERROR:
                    SwingUtilities.invokeLater(this::errorConnection);
                    break;
            }
        }, "update");
        controller.connect();
    }

    @Data
    @AllArgsConstructor
    private static class AnimationCell {
        private GUIField field;
        private GameModel model;
        private final int x, y;
    }
}
