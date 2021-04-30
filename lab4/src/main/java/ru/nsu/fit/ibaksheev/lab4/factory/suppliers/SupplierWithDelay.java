package ru.nsu.fit.ibaksheev.lab4.factory.suppliers;

import java.util.function.Supplier;

public abstract class SupplierWithDelay<T> implements Supplier<T> {
    private volatile int delay;

    public SupplierWithDelay(int delay) {
        this.delay = delay;
    }

    public void setDelay(int newDelay) {
        delay = newDelay;
    }

    public int getDelay() {
        return delay;
    }
}
