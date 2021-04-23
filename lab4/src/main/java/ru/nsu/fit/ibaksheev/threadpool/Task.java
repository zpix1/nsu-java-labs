package ru.nsu.fit.ibaksheev.threadpool;

public interface Task {
    String getName();
    void performWork() throws InterruptedException;
}
