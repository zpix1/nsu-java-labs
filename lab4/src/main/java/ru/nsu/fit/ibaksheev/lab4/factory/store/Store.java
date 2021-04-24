package ru.nsu.fit.ibaksheev.lab4.factory.store;

import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarPart;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Store<T> implements Supplier<T>, Consumer<T> {
    private final BlockingQueue<T> store;

    public Store(int storeSize) {
        store = new ArrayBlockingQueue<T>(storeSize);
    }

    @Override
    public T get() {
        try {
            return store.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void accept(T part) {
        try {
            store.put(part);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
