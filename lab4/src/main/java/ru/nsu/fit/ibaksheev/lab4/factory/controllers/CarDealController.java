package ru.nsu.fit.ibaksheev.lab4.factory.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.Car;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarAccessory;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarBody;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarEngine;
import ru.nsu.fit.ibaksheev.threadpool.Task;
import ru.nsu.fit.ibaksheev.threadpool.ThreadPool;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CarDealController extends Thread {
    private final Supplier<Car> outputStore;
    private final ThreadPool threadPool;
    private boolean isRunning = true;

    private final AtomicInteger totalSold = new AtomicInteger(0);

    public int getTotalSold() {
        return totalSold.intValue();
    }

    public CarDealController(Supplier<Car> outputStore) {
        this.outputStore = outputStore;

        threadPool = new ThreadPool(4, 1000);
    }

    public void shutdown() {
        isRunning = false;
        threadPool.shutdown();
    }

    public void joinAll() throws InterruptedException {
        threadPool.joinAll();
        this.join();
    }

    @Override
    public void run() {
        while (isRunning) {
            var car = outputStore.get();
            var task = new Task() {
                @Override
                public String getName() {
                    return "Sell a car";
                }

                @Override
                public void performWork() throws InterruptedException {

                    totalSold.incrementAndGet();
                }
            };
            threadPool.addTask(task);
        }
    }
}
