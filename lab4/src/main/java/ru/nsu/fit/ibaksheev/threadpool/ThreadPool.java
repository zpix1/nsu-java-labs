package ru.nsu.fit.ibaksheev.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

public class ThreadPool {
    private final BlockingQueue<Task> queue = new ArrayBlockingQueue<>(10);
    private boolean isRunning = true;

    public void addTask(Task command) {
        try {
            queue.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        isRunning = false;
    }

    private final class TaskWorker extends Thread {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    var task = queue.take();
                    task.performWork();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
