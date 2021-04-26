package ru.nsu.fit.ibaksheev.lab4.factory.gui;

import ru.nsu.fit.ibaksheev.lab4.factory.Factory;

import javax.swing.*;
import java.io.IOException;

public class FactoryGUI {
    public static void start() throws IOException {
        var window = new FactoryWindow();
        window.start();
    }
}
