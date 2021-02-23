package ru.nsu.fit.ibaksheev.minesweeper;

public interface ModelSubscriber<P> {
    void modelChanged(Model<P> model);
}
