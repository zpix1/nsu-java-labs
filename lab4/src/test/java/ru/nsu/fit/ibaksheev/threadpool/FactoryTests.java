package ru.nsu.fit.ibaksheev.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.ibaksheev.lab4.factory.Factory;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class FactoryTests {
    Logger logger = LogManager.getLogger(Factory.class);

    @Test
    public void factoryWorks() throws IOException, InterruptedException {
        var factory = new Factory();
        factory.start();
        Thread.sleep(30000);
        logger.info("shutting down");
        factory.shutdown(true);
        logger.info("done");
        assertNotEquals(0, factory.getTotalSold());
    }
}
