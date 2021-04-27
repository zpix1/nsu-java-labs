package ru.nsu.fit.ibaksheev.lab4.factory.suppliers;

import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarAccessory;

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
