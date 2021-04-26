package ru.nsu.fit.ibaksheev.lab4.factory.controllers;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.fit.ibaksheev.lab4.factory.store.Dealer;

import java.math.BigDecimal;

// Daemon thread
public class CarPriceController extends Thread {
    public final static int OK_CAR_PRICE = 500;

    private final Dealer dealer;
    private final FactoryProductionControlAdapter adapter;
    private final Logger logger = LogManager.getLogger();

    public CarPriceController(Dealer dealer, FactoryProductionControlAdapter adapter) {
        this.dealer = dealer;
        this.adapter = adapter;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            var carPrice = dealer.getCarPrice();
            if (isCarPriceOk(carPrice)) {
                logger.info(carPrice);
                if (adapter.isStopped()) {
                    logger.info("Car price is ok: " + carPrice + "; starting production back...");
                    adapter.continueProduction();
                }
            } else if (!adapter.isStopped()) {
                if (!adapter.isStopped()) {
                    logger.info("Car price is NOT ok: " + carPrice + "; stopping production...");
                    adapter.pauseProduction();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private boolean isCarPriceOk(BigDecimal carPrice) {
        return carPrice.compareTo(BigDecimal.valueOf(OK_CAR_PRICE)) > 0;
    }

    public interface FactoryProductionControlAdapter {
        boolean isStopped();

        void pauseProduction();

        void continueProduction();
    }
}
