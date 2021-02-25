package ru.nsu.fit.ibaksheev.minesweeper.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Model<P> {

    private P property;

    private final Multimap<String, ModelSubscriber<P>> subscribers = HashMultimap.create();

    public Model(P property) {
        this.property = property;
    }

    protected P getProperty() {
        if (property == null) {
            throw new NullPointerException("Attempt to get null property");
        }
        return property;
    }

    protected void setProperty(P property) {
        if (property == null) {
            throw new NullPointerException("Attempt to set null property");
        }
        this.property = property;
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
//        notifySubscriber(subscriber);
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
