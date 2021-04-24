package ru.nsu.fit.ibaksheev.lab4.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarAccessory;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarBody;
import ru.nsu.fit.ibaksheev.lab4.factory.parts.CarEngine;
import ru.nsu.fit.ibaksheev.lab4.factory.store.Store;
import ru.nsu.fit.ibaksheev.lab4.factory.suppliers.CarAccessorySupplier;
import ru.nsu.fit.ibaksheev.lab4.factory.suppliers.CarBodySupplier;
import ru.nsu.fit.ibaksheev.lab4.factory.suppliers.CarEngineSupplier;

import java.io.IOException;
import java.util.Properties;

public class Factory {
    private final static String propertiesFilename = "factory.properties";
    private final Logger logger = LogManager.getLogger();

    private final CarBodySupplier carBodySupplier;
    private final CarEngineSupplier carEngineSupplier;
    private final CarAccessorySupplier carAccessorySupplier;

    private final Store<CarBody> carBodyStore;
    private final Store<CarEngine> carEngineStore;
    private final Store<CarAccessory> carAccessoryStore;

    public Factory() throws IOException {
        var properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(propertiesFilename));
        } catch (IOException e) {
            logger.error("Can't load " + propertiesFilename);
            throw e;
        }

        carBodySupplier = new CarBodySupplier();
        carBodyStore = new Store<CarBody>(Integer.parseInt((String) properties.get("CAR_BODY_STORE_SIZE")));

        carEngineSupplier = new CarEngineSupplier();
        carEngineStore = new Store<CarEngine>(Integer.parseInt((String) properties.get("CAR_ENGINE_STORE_SIZE")));

        carAccessorySupplier = new CarAccessorySupplier();
        carAccessoryStore = new Store<CarAccessory>(Integer.parseInt((String) properties.get("CAR_ACCESSORY_STORE_SIZE")));

        logger.info("Stores and suppliers created");
    }
}
