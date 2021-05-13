package ru.nsu.fit.ibaksheev.lab4.factory.store;

import ru.nsu.fit.ibaksheev.myblockingqueue.MyBlockingQueue;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Store<T> implements Supplier<T>, Consumer<T> {
    private final int storeSize;
    private final MyBlockingQueue<T> store;

    public Store(int storeSize) {
        this.storeSize = storeSize;
        store = new MyBlockingQueue<>(storeSize);
    }

    public int getSize() {
        return store.size();
    }

    public int getCapacity() {
        return storeSize;
    }

    @Override
    public T get() {
        try {
            return store.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    public void accept(T part) {
        try {
            store.put(part);
        } catch (InterruptedException ignored) {
        }
    }
}
