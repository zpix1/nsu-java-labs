package ru.nsu.fit.ibaksheev.lab4.factory.suppliers;

import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarBody;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarPart;

import java.util.function.Supplier;

public class CarBodySupplier implements Supplier<CarBody> {
    private final int delay;

    public CarBodySupplier(int delay) {
        this.delay = delay;
    }

    public CarBody get() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            return null;
        }
        return new CarBody();
    }
}
