package ru.nsu.fit.ibaksheev.lab4.factory.suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarEngine;

import java.util.function.Supplier;

public class CarEngineSupplier implements Supplier<CarEngine> {
    private final int delay;

    public CarEngineSupplier(int delay) {
        this.delay = delay;
    }

    public CarEngine get() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new CarEngine();
    }
}
