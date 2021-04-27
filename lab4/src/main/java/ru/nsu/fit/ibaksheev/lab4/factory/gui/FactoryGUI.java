package ru.nsu.fit.ibaksheev.lab4.factory.gui;

import java.io.IOException;

public class FactoryGUI {
    public static void start() throws IOException {
        var window = new FactoryWindow();
        window.start();
    }
}
