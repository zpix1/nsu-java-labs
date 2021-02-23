package ru.nsu.fit.ibaksheev.minesweeper;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class Model<P> {
    private P property;

    private final Collection<ModelSubscriber<P>> subscribers = new CopyOnWriteArrayList<ModelSubscriber<P>>();

    public Model(P property) {
        if (property == null) {
            throw new NullPointerException("Null property");
        }
        this.property = property;
    }

    public P getProperty() {
        return property;
    }

    protected void notifySubscribers() {
        for (final ModelSubscriber<P> subscriber : subscribers) {
            notifySubscriber(subscriber);
        }
    }

    private void notifySubscriber(ModelSubscriber<P> subscriber) {
        subscriber.modelChanged(this);
    }

    public void subscribe(ModelSubscriber<P> subscriber) {
        if (subscriber == null) {
            throw new NullPointerException("Void subscriber");
        }
        if (subscribers.contains(subscriber)) {
            throw new IllegalArgumentException("Repeated subscribe: " + subscriber);
        }
        subscribers.add(subscriber);
        notifySubscriber(subscriber);
    }

    public void unsubscribe(ModelSubscriber<P> subscriber) {
        if (subscriber == null) {
            throw new NullPointerException("Void subscriber");
        }
        if (!subscribers.contains(subscriber)) {
            throw new IllegalArgumentException("No such subscriber: " + subscriber);
        }
        subscribers.remove(subscriber);
    }
}
