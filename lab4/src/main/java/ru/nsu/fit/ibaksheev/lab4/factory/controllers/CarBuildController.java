package ru.nsu.fit.ibaksheev.lab4.factory.controllers;

import ru.nsu.fit.ibaksheev.lab4.factory.parts.Car;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarAccessory;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarBody;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarEngine;
import ru.nsu.fit.ibaksheev.threadpool.Task;
import ru.nsu.fit.ibaksheev.threadpool.ThreadPool;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CarBuildController extends Thread {
    private final Supplier<CarBody> carBodyStore;
    private final Supplier<CarEngine> carEngineStore;
    private final Supplier<CarAccessory> carAccessoryStore;
    private final Consumer<Car> outputStore;
    private final ThreadPool threadPool;
    private boolean isRunning = true;

    public CarBuildController(Supplier<CarBody> carBodyStore, Supplier<CarEngine> carEngineStore, Supplier<CarAccessory> carAccessoryStore, Consumer<Car> outputStore) {
        this.carBodyStore = carBodyStore;
        this.carEngineStore = carEngineStore;
        this.carAccessoryStore = carAccessoryStore;
        this.outputStore = outputStore;

        threadPool = new ThreadPool(4, 1000);
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
                public void performWork() throws InterruptedException {
                    var car = new Car(body, engine, accessory);
                    outputStore.accept(car);
                }
            };

            threadPool.addTask(task);
        }
    }
}
