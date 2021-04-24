package ru.nsu.fit.ibaksheev.lab4.factory.suppliers;

import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarBody;

public class CarBodySupplier implements CarPartSupplier {
    public CarBody get() {
        return new CarBody();
    }
}
