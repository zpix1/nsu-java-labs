package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import ru.nsu.fit.ibaksheev.minesweeper.model.FieldCellState;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;

public class GUIFieldCell extends JButton {
    private FieldCellState state;
    private final int vx;
    private final int vy;

    public GUIFieldCell(int x, int y) {
        this.vx = x;
        this.vy = y;
    }

    public void setState(FieldCellState state) {
        if (state.type == FieldCellState.Type.Unknown) {
            setText(" ");
        } else if (state.type == FieldCellState.Type.Empty) {
            if (state.value == 0) {
                setText(" ");
            } else {
                setText(String.valueOf(Integer.toString(state.value).charAt(0)));
                setFont(getFont().deriveFont(Font.BOLD));
                setFont(getFont().deriveFont(20f));
                final Color c;
                switch (state.value) {
                    case 1:
                        c = Color.blue;
                        break;
                    case 2:
                        c = new Color(60, 63, 65);
                        break;
                    case 3:
                        c = new Color(216, 86, 77);
                        break;
                    case 4:
                        c = new Color(123, 31, 162);
                        break;
                    case 5:
                        c = new Color(247, 160, 51);
                        break;
                    case 6:
                    case 7:
                    case 8:
                        c = Color.red;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + state.value);
                }
                setUI(new MetalButtonUI() {
                    protected Color getDisabledTextColor() {
                        return c;
                    }
                });
            }
            setEnabled(false);
        } else if (state.type == FieldCellState.Type.Flag) {
            setText("F");
        } else if (state.type == FieldCellState.Type.Mine) {
            setText("*");
        }
    }

    public int getFieldX() {
        return vx;
    }

    public int getFieldY() {
        return vy;
    }
}
