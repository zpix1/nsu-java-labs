package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import ru.nsu.fit.ibaksheev.minesweeper.model.FieldCellState;

import javax.swing.*;

public class GUIFieldCell extends JButton {
    private FieldCellState state;
    private int vx, vy;

    public GUIFieldCell(int x, int y) {
        this.vx = x;
        this.vy = y;
    }

    public void setState(FieldCellState state) {
        if (state.type == FieldCellState.Type.Unknown) {
            setText(".");
        } else if (state.type == FieldCellState.Type.Empty) {
            if (state.value == 0) {
                setText(" ");
            } else {
                setText(String.valueOf(Integer.toString(state.value).charAt(0)));
            }
        } else if (state.type == FieldCellState.Type.Flag) {
            setText("F");
        }
    }

    public int getFieldX() {
        return vx;
    }

    public int getFieldY() {
        return vy;
    }
}
