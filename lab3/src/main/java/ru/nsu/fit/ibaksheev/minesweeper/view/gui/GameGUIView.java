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

    public GameGUIView(GameModel model, GameController controller) {
        super(model, controller);
    }

    MouseAdapter adapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            var cell = (GUIFieldCell) e.getSource();
            System.out.println(cell.getFieldX() + " " + cell.getFieldY());
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

    @Override
    public void start() {
        controller.newGame(5, 5, 1);

        window = new JFrame("Minesweeper");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 500);


        var field = new GUIField();

        model.subscribe(model -> {
            lost();
        }, "lost");

        model.subscribe(model -> {
            won();
        }, "won");

        model.subscribe(model -> {
            field.updateField(this.model.getPlayerField());
        }, "wholeFieldUpdate");

        model.subscribe(model -> {
            System.out.println("reset");
            System.out.println(this.model.getPlayerField().length);
            field.setField(this.model.getPlayerField(), adapter);
            window.setVisible(true);
        }, "reset");

        field.setField(model.getPlayerField(), adapter);

        window.add(field);

        window.setVisible(true);
    }

    private void lost() {
        JOptionPane.showInternalMessageDialog(null, "You lost...",
                "I am sorry, but you just exploded!", JOptionPane.INFORMATION_MESSAGE);
        controller.newGame(9, 9, 10);
    }

    private void won() {
        JOptionPane.showInternalMessageDialog(null, "You won!",
                "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        controller.newGame(9, 9, 10);
    }

}
