package ru.nsu.fit.ibaksheev.lab4.factory.suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarAccessory;

import java.util.function.Supplier;

public class CarAccessorySupplier extends SupplierWithDelay<CarAccessory> {
    public CarAccessorySupplier(int delay) {
        super(delay);
    }

    public CarAccessory get() {
        try {
            Thread.sleep(getDelay());
        } catch (InterruptedException e) {
            return null;
        }
        return new CarAccessory();
    }
}
