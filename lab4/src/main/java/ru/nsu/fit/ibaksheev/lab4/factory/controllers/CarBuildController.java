package ru.nsu.fit.ibaksheev.lab4.factory.controllers;

import lombok.SneakyThrows;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.Car;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarAccessory;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarBody;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarEngine;
import ru.nsu.fit.ibaksheev.threadpool.Task;
import ru.nsu.fit.ibaksheev.threadpool.ThreadPool;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CarBuildController extends Thread {
    private final Supplier<CarBody> carBodyStore;
    private final Supplier<CarEngine> carEngineStore;
    private final Supplier<CarAccessory> carAccessoryStore;
    private final Consumer<Car> outputStore;
    private final ThreadPool threadPool;
    private volatile boolean isRunning = true;

    private final Object pauseObject = new Object();
    private volatile Boolean pause = false;

    public Boolean isPaused() {
        return pause;
    }

    public CarBuildController(Supplier<CarBody> carBodyStore, Supplier<CarEngine> carEngineStore, Supplier<CarAccessory> carAccessoryStore, Consumer<Car> outputStore) {
        this.carBodyStore = carBodyStore;
        this.carEngineStore = carEngineStore;
        this.carAccessoryStore = carAccessoryStore;
        this.outputStore = outputStore;

        threadPool = new ThreadPool(4, 1000);
    }

    public void pauseProduction() {
        if (pause) throw new RuntimeException("double pause detected");
        pause = true;
    }

    public void continueProduction() {
        if (!pause) throw new RuntimeException("double continue detected");
        pause = false;
        pauseObject.notifyAll();
    }

    public void shutdown() {
        isRunning = false;
        threadPool.shutdown();
    }

    public void interruptAll() {
        threadPool.interruptAll();
        this.interrupt();
    }

    @SneakyThrows
    @Override
    public void run() {
        while (isRunning) {
            var body = carBodyStore.get();
            var engine = carEngineStore.get();
            var accessory = carAccessoryStore.get();

            if (body == null || engine == null || accessory == null) {
                continue;
            }

            var task = new Task() {
                @Override
                public String getName() {
                    return "Build a car";
                }

                @Override
                public void performWork() {
                    var car = new Car(body, engine, accessory);
                    outputStore.accept(car);
                }
            };

            threadPool.addTask(task);

            if (pause) {
                synchronized (pauseObject) {
                    try {
                        do {
                            pauseObject.wait();
                        } while (pause);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }
}
