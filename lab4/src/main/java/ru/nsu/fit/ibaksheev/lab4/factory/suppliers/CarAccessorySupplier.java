package ru.nsu.fit.ibaksheev.lab4.factory.suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarAccessory;

import java.util.function.Supplier;

public class CarAccessorySupplier implements Supplier<CarAccessory> {
    private final int delay;

    public CarAccessorySupplier(int delay) {
        this.delay = delay;
    }

    public CarAccessory get() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            return null;
        }
        return new CarAccessory();
    }
}
