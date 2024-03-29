package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import lombok.Getter;
import ru.nsu.fit.ibaksheev.minesweeper.model.FieldCellState;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.util.Objects;

public class GUIFieldCell extends JButton {
    public static final int CELL_SIZE = 45;
    public static final float FONT_SIZE = 15f;
    private static final ImageIcon FLAG_ICON = new ImageIcon(Objects.requireNonNull(Objects.requireNonNull(GUIFieldCell.class.getResource("/images/flag.png"))));
    private static final ImageIcon MINE_ICON = new ImageIcon(Objects.requireNonNull(Objects.requireNonNull(GUIFieldCell.class.getResource("/images/mine.png"))));
    @Getter
    private final int fieldX;
    @Getter
    private final int fieldY;

    public GUIFieldCell(int x, int y) {
        setSize(CELL_SIZE, CELL_SIZE);
        setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        this.fieldX = x;
        this.fieldY = y;
    }

    public void setState(FieldCellState state) {
        setFont(getFont().deriveFont(Font.BOLD));
        setFont(getFont().deriveFont(FONT_SIZE));
        if (state.getType() == FieldCellState.Type.Unknown) {
            setIcon(null);
            setText("");
        } else if (state.getType() == FieldCellState.Type.Empty) {
            setIcon(null);
            if (state.getValue() == 0) {
                setText(" ");
            } else {
                setText(String.valueOf(Integer.toString(state.getValue()).charAt(0)));
                final Color c;
                switch (state.getValue()) {
                    case 1:
                        c = Color.blue;
                        break;
                    case 2:
                        c = new Color(38, 120, 46);
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
                        throw new IllegalStateException("Unexpected value: " + state.getValue());
                }
                setUI(new MetalButtonUI() {
                    protected Color getDisabledTextColor() {
                        return c;
                    }
                });
            }
            setEnabled(false);
        } else if (state.getType() == FieldCellState.Type.Flag) {
            setIcon(FLAG_ICON);
//            setText("F");
        } else if (state.getType() == FieldCellState.Type.Mine) {
            setIcon(MINE_ICON);
//            setText("*");
            setEnabled(false);
        }
    }
}
