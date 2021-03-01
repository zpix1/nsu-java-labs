package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import lombok.Getter;
import ru.nsu.fit.ibaksheev.minesweeper.model.FieldCellState;

import java.awt.*;
import java.awt.event.MouseAdapter;

public class GUIField extends Container {
    private int width;
    private int height;

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setField(FieldCellState[][] field, MouseAdapter fieldCellMouseListener) {
        this.width = field[0].length * GUIFieldCell.CELL_SIZE;
        this.height = field.length * GUIFieldCell.CELL_SIZE;
        removeAll();
        setLayout(new GridLayout(field.length, field[0].length));
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                var cell = new GUIFieldCell(i, j);
                cell.addMouseListener(fieldCellMouseListener);
                cell.setState(field[i][j]);
                cell.setFocusPainted(false);
                add(cell);
            }
        }
    }

    public void updateField(FieldCellState[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                ((GUIFieldCell) getComponent(i * field[0].length + j)).setState(field[i][j]);
            }
        }
    }

    public void updateFieldCell(FieldCellState[][] field, int updatedFieldCellX, int updatedFieldCellY) {
        ((GUIFieldCell) getComponent(updatedFieldCellX * field[0].length + updatedFieldCellY)).setState(field[updatedFieldCellX][updatedFieldCellY]);
    }
}
