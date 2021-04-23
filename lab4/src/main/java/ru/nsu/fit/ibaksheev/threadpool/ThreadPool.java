package ru.nsu.fit.ibaksheev.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

public class ThreadPool {
    private final BlockingQueue<Task> queue = new ArrayBlockingQueue<>(10);

    public void addTask(Task command) {
        try {
            queue.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
