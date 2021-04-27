package ru.nsu.fit.ibaksheev.lab4.factory.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.Car;
import ru.nsu.fit.ibaksheev.lab4.factory.store.Dealer;
import ru.nsu.fit.ibaksheev.threadpool.Task;
import ru.nsu.fit.ibaksheev.threadpool.ThreadPool;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class CarDealController extends Thread {
    private final Supplier<Car> outputStore;
    private final ThreadPool threadPool;
    private final AtomicInteger totalSold = new AtomicInteger(0);
    private BigDecimal totalGain = new BigDecimal(0);

    private final Dealer dealer;
    private Logger logger = LogManager.getLogger();
    private boolean isRunning = true;

    public CarDealController(Supplier<Car> outputStore, Dealer dealer) {
        this.dealer = dealer;
        this.outputStore = outputStore;

        threadPool = new ThreadPool(4, 1000);
    }

    public int getTotalSold() {
        return totalSold.intValue();
    }

    public BigDecimal getTotalGain() {
        return totalGain;
    }

    private synchronized void addMoney(BigDecimal gain) {
        totalGain = totalGain.add(gain);
    }

    public void shutdown() {
        isRunning = false;
        threadPool.shutdown();
    }

    public void interruptAll() {
        threadPool.interruptAll();
        this.interrupt();
    }

    @Override
    public void run() {
        while (isRunning) {
            var car = outputStore.get();
            if (car == null) {
                continue;
            }

            var task = new Task() {
                @Override
                public String getName() {
                    return "Sell a car";
                }

                @Override
                public void performWork() {
                    totalSold.incrementAndGet();
                    addMoney(dealer.sell(car));
                }
            };
            threadPool.addTask(task);
        }
    }
}
