package ru.nsu.fit.ibaksheev.minesweeper.model;

import java.io.Serializable;

public class FieldCellState implements Serializable {
    public Type type;
    public int value;
    public FieldCellState(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    public enum Type {
        Empty,
        Unknown,
        Flag,
        Mine
    }
}
