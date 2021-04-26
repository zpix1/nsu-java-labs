package ru.nsu.fit.ibaksheev.lab4.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.fit.ibaksheev.lab4.factory.controllers.CarBuildController;
import ru.nsu.fit.ibaksheev.lab4.factory.controllers.CarDealController;
import ru.nsu.fit.ibaksheev.lab4.factory.controllers.CarPriceController;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.*;
import ru.nsu.fit.ibaksheev.lab4.factory.store.Dealer;
import ru.nsu.fit.ibaksheev.lab4.factory.store.Store;
import ru.nsu.fit.ibaksheev.lab4.factory.suppliers.CarAccessorySupplier;
import ru.nsu.fit.ibaksheev.lab4.factory.suppliers.CarBodySupplier;
import ru.nsu.fit.ibaksheev.lab4.factory.suppliers.CarEngineSupplier;

import java.io.IOException;
import java.util.Properties;

public class Factory {
    private final static String propertiesFilename = "/factory.properties";
    private final Logger logger = LogManager.getLogger();

    private final CarBodySupplier carBodySupplier;
    private final CarEngineSupplier carEngineSupplier;
    private final CarAccessorySupplier carAccessorySupplier;

    private final Store<CarBody> carBodyStore;
    private final Store<CarEngine> carEngineStore;
    private final Store<CarAccessory> carAccessoryStore;
    private final Store<Car> carStore;

    private final ProductionThread<CarBody> carBodyProductionThread;
    private final ProductionThread<CarEngine> carEngineProductionThread;
    private final ProductionThread<CarAccessory> carAccessoryProductionThread;

    private final CarBuildController carBuildController;
    private final CarDealController carDealController;
    private final CarPriceController carPriceController;

    private final Dealer dealer;

    public int getTotalSold() {
        return carDealController.getTotalSold();
    }

    public Factory() throws IOException {
        var properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(propertiesFilename));
        } catch (IOException e) {
            logger.error("Can't load " + propertiesFilename);
            throw e;
        }

        carStore = new Store<>(Integer.parseInt((String) properties.get("CAR.STORE_SIZE")));

        carBodySupplier = new CarBodySupplier(Integer.parseInt((String) properties.get("CAR_BODY.DELAY")));
        carBodyStore = new Store<>(Integer.parseInt((String) properties.get("CAR_BODY.STORE_SIZE")));
        carBodyProductionThread = new ProductionThread<>(carBodySupplier, carBodyStore);

        carEngineSupplier = new CarEngineSupplier(Integer.parseInt((String) properties.get("CAR_ENGINE.DELAY")));
        carEngineStore = new Store<>(Integer.parseInt((String) properties.get("CAR_ENGINE.STORE_SIZE")));
        carEngineProductionThread = new ProductionThread<>(carEngineSupplier, carEngineStore);

        carAccessorySupplier = new CarAccessorySupplier(Integer.parseInt((String) properties.get("CAR_ACCESSORY.DELAY")));
        carAccessoryStore = new Store<>(Integer.parseInt((String) properties.get("CAR_ACCESSORY.STORE_SIZE")));
        carAccessoryProductionThread = new ProductionThread<>(carAccessorySupplier, carAccessoryStore);

        dealer = new Dealer();

        logger.info("Stores, suppliers and threads created");

        carBuildController = new CarBuildController(carBodyStore, carEngineStore, carAccessoryStore, carStore);
        carPriceController = new CarPriceController(dealer, new CarPriceController.FactoryProductionControlAdapter() {
            @Override
            public boolean isStopped() {
                return carBuildController.isPaused();
            }

            @Override
            public void pauseProduction() {
                carBuildController.pauseProduction();
            }

            @Override
            public void continueProduction() {
                carBuildController.continueProduction();
            }
        });
        carDealController = new CarDealController(carStore, dealer);

        logger.info("Controllers created");
    }

    public void start() {
        carBodyProductionThread.start();
        carEngineProductionThread.start();
        carAccessoryProductionThread.start();
        carPriceController.start();

        logger.info("Production threads started");

        carBuildController.start();
        carDealController.start();

        logger.info("Controllers started");
    }

    public void shutdown(boolean interrupt) {
        logger.info("Shutdown issued");

        carBodyProductionThread.shutdown();
        carEngineProductionThread.shutdown();
        carAccessoryProductionThread.shutdown();

        carBuildController.shutdown();
        carDealController.shutdown();

        if (interrupt) {
            logger.info("Interrupting producer threads");

            carBodyProductionThread.interrupt();
            carEngineProductionThread.interrupt();
            carAccessoryProductionThread.interrupt();

            logger.info("Interrupting controller threads");

            carBuildController.interruptAll();
            carDealController.interruptAll();
        }
    }
}
