package ru.nsu.fit.ibaksheev.threadpool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


public class ThreadPoolTests {
    @Test
    public void itDoesItsWork() throws InterruptedException {
        var tp = new ThreadPool(4, 1024);
        var queue = new ArrayBlockingQueue<Double>(100);

        for (int i = 0; i < 50; i++) {
            int finalI = i;
            var task = new Task() {
                @Override
                public String getName() {
                    return Integer.toString(finalI);
                }

                @Override
                public void performWork() throws InterruptedException {
                    Thread.sleep(100);
                    queue.add(Math.sqrt(finalI));
                }
            };
            tp.addTask(task);
        }

        for (int i = 0; i < 50; i++) {
            var result = queue.poll(1000, TimeUnit.MILLISECONDS);
            assertNotNull(result, "result should be obtained in 1 second");
        }

        tp.shutdown();
    }
}
