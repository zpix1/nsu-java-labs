package ru.nsu.fit.ibaksheev.minesweeper.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Setter(AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
public class Model<P> implements Serializable {
    @NonNull
    private P property;

    private transient Multimap<String, ModelSubscriber<P>> subscribers = HashMultimap.create();

    public Model(P property) {
        this.property = property;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        subscribers = HashMultimap.create();
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        objectOutputStream.defaultWriteObject();
    }

    public boolean propertyExists() {
        return property != null;
    }

    protected void notifySubscribers(String event) {
        for (final ModelSubscriber<P> subscriber : subscribers.get(event)) {
            notifySubscriber(subscriber);
        }
    }

    private void notifySubscriber(ModelSubscriber<P> subscriber) {
        subscriber.modelChanged(this);
    }

    public void subscribe(ModelSubscriber<P> subscriber, String event) {
        if (subscriber == null) {
            throw new NullPointerException("Void subscriber");
        }
        if (subscribers.get(event).contains(subscriber)) {
            throw new IllegalArgumentException("Repeated subscribe: " + subscriber);
        }
        subscribers.get(event).add(subscriber);
    }

    public void unsubscribe(ModelSubscriber<P> subscriber, String event) {
        if (subscriber == null) {
            throw new NullPointerException("Void subscriber");
        }
        if (!subscribers.get(event).contains(subscriber)) {
            throw new IllegalArgumentException("No such subscriber: " + subscriber);
        }
        subscribers.get(event).remove(subscriber);
    }
}
