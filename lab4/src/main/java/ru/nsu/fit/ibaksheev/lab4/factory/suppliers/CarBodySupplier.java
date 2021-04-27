package ru.nsu.fit.ibaksheev.lab4.factory.suppliers;

import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarBody;

public class CarBodySupplier extends SupplierWithDelay<CarBody> {
    public CarBodySupplier(int delay) {
        super(delay);
    }

    public CarBody get() {
        try {
            Thread.sleep(getDelay());
        } catch (InterruptedException e) {
            return null;
        }
        return new CarBody();
    }
}
