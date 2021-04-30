package ru.nsu.fit.ibaksheev.lab4.factory.store;

import ru.nsu.fit.ibaksheev.lab4.factory.parts.Car;

import java.math.BigDecimal;
import java.util.Random;

public class Dealer {
    private static final int DEFAULT_CAR_PRICE = 500;
    private static final int DEFAULT_CAR_DELTA = 100;
    private static final Random random = new Random();
    private final BigDecimal delta = new BigDecimal(DEFAULT_CAR_DELTA);

    private BigDecimal carPrice = new BigDecimal(DEFAULT_CAR_PRICE);

    public BigDecimal getCarPrice() {
        return carPrice;
    }

    public Dealer() {
        var priceChangerThread = new Thread(this::priceChanger);
        priceChangerThread.setDaemon(true);
        priceChangerThread.start();
    }

    private void priceChanger() {
        while (true) {
            double deltaK = random.nextGaussian();
            // change to private lock object if needed
            synchronized (this) {
                var newPrice = carPrice.add(delta.multiply(BigDecimal.valueOf(deltaK)));
                if (newPrice.compareTo(BigDecimal.ZERO) > 0) {
                    carPrice = carPrice.add(delta.multiply(BigDecimal.valueOf(deltaK)));
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public BigDecimal sell(Car car) {
        return carPrice;
    }
}
