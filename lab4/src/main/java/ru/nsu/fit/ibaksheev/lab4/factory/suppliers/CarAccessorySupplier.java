package ru.nsu.fit.ibaksheev.lab4.factory.suppliers;

import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarAccessory;

public class CarAccessorySupplier implements CarPartSupplier {
    public CarAccessory get() {
        return new CarAccessory();
    }
}
