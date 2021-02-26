package ru.nsu.fit.ibaksheev.minesweeper.view.gui;

import ru.nsu.fit.ibaksheev.minesweeper.model.FieldCellState;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class GUIField extends JPanel {

    public GUIField() {
    }

    public void setField(FieldCellState[][] field, MouseAdapter fieldCellMouseListener) {
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
                ((GUIFieldCell)getComponent(i * field[0].length + j)).setState(field[i][j]);
//                var c = (GUIFieldCell)getComponent(i * field.length + j);
//                System.out.println(c.getFieldX() + " " + c.getFieldY());
            }
        }
    }
}