package ru.nsu.fit.ibaksheev.lab4.factory.parts;

public class Car {
    private final CarBody body;
    private final CarEngine engine;
    private final CarAccessory accessory;

    public Car(CarBody body, CarEngine engine, CarAccessory accessory) {
        this.body = body;
        this.engine = engine;
        this.accessory = accessory;
    }

    public String toString() {
        return String.format("Car %d (Body: %d, Engine: %d, Accessory: %d)", this.hashCode(), this.body.hashCode(), this.engine.hashCode(), this.accessory.hashCode());
    }
}
