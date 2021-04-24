package ru.nsu.fit.ibaksheev.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

public class ThreadPool {
    private final BlockingQueue<Task> queue;
    private boolean isRunning = true;
    private final Logger logger = LogManager.getLogger();
    private final Set<TaskWorker> workers = new HashSet<>();

    public ThreadPool(int threadCount, int queueSize) {
        queue = new ArrayBlockingQueue<>(queueSize);
        for (int i = 0; i < threadCount; i++) {
            var tw = new TaskWorker();
            tw.start();
            workers.add(tw);
        }
    }

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

    public void joinAll() throws InterruptedException {
        if (isRunning) throw new RuntimeException("can't join running threads");
        for (var t: workers) {
            t.join();
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
