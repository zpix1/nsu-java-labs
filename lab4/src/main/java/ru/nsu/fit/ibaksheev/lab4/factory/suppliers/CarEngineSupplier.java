package ru.nsu.fit.ibaksheev.lab4.factory.suppliers;

import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarEngine;

public class CarEngineSupplier implements CarPartSupplier {
    public CarEngine get() {
        return new CarEngine();
    }
}
