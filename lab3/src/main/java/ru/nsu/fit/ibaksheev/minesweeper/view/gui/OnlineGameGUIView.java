package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.nsu.fit.ibaksheev.minesweeper.controller.OnlineGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.Model;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;
import ru.nsu.fit.ibaksheev.minesweeper.view.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class OnlineGameGUIView implements GameView {
    private static final int ANIMATION_STEP = 0;
    private final GameModel syncModel;
    private final GameModel model;
    private final Model<OnlineGameController.NetworkState> networkModel;
    private final OnlineGameController controller;
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
    private JFrame window;
    private GUIField field;
    private GUIField syncField;
    private JLabel label;

    public OnlineGameGUIView(OnlineGameController controller) {
        this.controller = controller;
        model = controller.getModel();
        syncModel = controller.getSyncModel();
        networkModel = controller.getNetworkModel();
    }

    private void afterDisconnection() {
        window.removeAll();

        var loadingLabel = new JLabel("Disconnected...");
        window.add(loadingLabel, BorderLayout.CENTER);

        window.pack();
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(this::init);
        networkModel.subscribe(model -> {
            System.out.println("updateProp");
            var newState = networkModel.getProperty();
            switch (newState) {
                case CONNECTED:
                    SwingUtilities.invokeLater(this::afterConnection);
                    break;
                case WAITING_FOR_OPPONENT:
                    SwingUtilities.invokeLater(this::waitConnection);
                    break;
                case OPPONENT_LOST:
                    endGame();
                    SwingUtilities.invokeLater(this::opponentLost);
                    break;
                case OPPONENT_WON:
                    endGame();
                    SwingUtilities.invokeLater(this::opponentWon);
                    break;
                case DISCONNECTED:
                    endGame();
                    break;
                case ERROR:
                    System.out.println("ERROR");
                    controller.endGame();
                    SwingUtilities.invokeLater(this::errorConnection);
                    break;
            }
        }, "update");
        controller.connect();
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

        model.subscribe(temp -> animationQueue.add(new AnimationCell(field, model, model.getUpdatedFieldCellX(), model.getUpdatedFieldCellY())), "fieldCellUpdate");

        syncModel.subscribe(temp -> animationQueue.add(new AnimationCell(syncField, syncModel, syncModel.getUpdatedFieldCellX(), syncModel.getUpdatedFieldCellY())), "fieldCellUpdate");

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

    private void opponentLost() {
        endGame();
        JOptionPane.showInternalMessageDialog(null, "You won! (your opponent lost)",
                "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void opponentWon() {
        endGame();
        JOptionPane.showInternalMessageDialog(null, "You lost! (your opponent won)",
                "Bad!", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void endGame() {
        window.setVisible(false);
        controller.endGame();
    }

    public void errorConnection() {
        endGame();
        JOptionPane.showInternalMessageDialog(null, "Error!",
                "Error!", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void lost() {
        endGame();
        JOptionPane.showInternalMessageDialog(null, "You lost!",
                "Bad!", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void won() {
        endGame();
        JOptionPane.showInternalMessageDialog(null, "You won!",
                "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    @Data
    @AllArgsConstructor
    private static class AnimationCell {
        private GUIField field;
        private GameModel model;
        private final int x, y;
    }
}
