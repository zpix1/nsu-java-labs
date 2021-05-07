package ru.nsu.fit.ibaksheev.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;

public class ThreadPool {
    private final static Logger logger = LogManager.getLogger();

    private final BlockingQueue<Task> queue;
    private final Set<TaskWorker> workers = new HashSet<>();
    private volatile boolean isRunning = true;

    public ThreadPool(int threadCount, int queueSize) {
        queue = new ArrayBlockingQueue<>(queueSize);
        for (int i = 0; i < threadCount; i++) {
            var tw = new TaskWorker();
            tw.setName(Integer.toString(i));
            tw.start();
            workers.add(tw);
        }
    }

    public void addTask(Task command) {
        if (!isRunning) {
            throw new RejectedExecutionException();
        }
        try {
            queue.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        isRunning = false;
        interruptAll();
        for (var t : workers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException("double interrupt detected");
            }
        }
    }

    public void interruptAll() {
        if (isRunning) throw new RuntimeException("can't join interrupt threads");
        for (var t : workers) {
            t.interrupt();
        }
    }

    private final class TaskWorker extends Thread {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    var task = queue.take();
                    logger.debug("Executing task '" + task.getName() + "'");
                    task.performWork();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
