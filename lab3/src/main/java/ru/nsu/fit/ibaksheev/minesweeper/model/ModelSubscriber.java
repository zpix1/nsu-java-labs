package ru.nsu.fit.ibaksheev.minesweeper.model;

public interface ModelSubscriber<P> {
    void modelChanged(Model<P> model);
}
