package ru.nsu.fit.ibaksheev.lab4.factory.suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarEngine;

import java.util.function.Supplier;

public class CarEngineSupplier extends SupplierWithDelay<CarEngine> {
    public CarEngineSupplier(int delay) {
        super(delay);
    }

    public CarEngine get() {
        try {
            Thread.sleep(getDelay());
        } catch (InterruptedException e) {
            return null;
        }
        return new CarEngine();
    }
}
