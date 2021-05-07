package ru.nsu.fit.ibaksheev.lab4.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class FactoryTests {
    final Logger logger = LogManager.getLogger(Factory.class);

    @Test
    public void factoryWorks() throws IOException, InterruptedException {
        var factory = new Factory();
        factory.start();
        Thread.sleep(30000);
        logger.info("shutting down");
        factory.shutdown();
        logger.info("done");
        assertNotEquals(0, factory.getTotalSold(), "factory should sell cars");
        assertTrue(factory.getTotalGain().doubleValue() / factory.getTotalSold() > 500, "factory should be efficient");
    }
}
