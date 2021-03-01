package ru.nsu.fit.ibaksheev.minesweeper.model;

import lombok.*;

import java.io.Serializable;

@Data
public class FieldCellState implements Serializable {
    private Type type;
    private int value;

    public FieldCellState(Type type, int value) {
        this.setType(type);
        this.setValue(value);
    }

    public enum Type {
        Empty,
        Unknown,
        Flag,
        Mine
    }
}
