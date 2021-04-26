package ru.nsu.fit.ibaksheev.lab4.factory.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.fit.ibaksheev.lab4.factory.Factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.DecimalFormat;

public class FactoryWindow extends JFrame {
    private final Logger logger = LogManager.getLogger();

    private Factory factory;

    private JLabel carPriceLabel;
    private JLabel carBodyStoreSizeLabel;
    private JLabel carEngineStoreSizeLabel;
    private JLabel carAccessoryStoreSizeLabel;
    private JLabel totalSoldLabel;
    private JLabel buildStateLabel;

    public FactoryWindow() {
        super("Factory");
    }

    public void start() throws IOException {
        factory = new Factory();
        factory.start();

        SwingUtilities.invokeLater(this::init);
    }

    @Override
    public void dispose() {
        factory.shutdown(true);
        System.exit(0);
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        var mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        {
            var storageStatePanel = new BorderedPanel("Storage state");
            mainPanel.add(storageStatePanel, BorderLayout.NORTH);

            carBodyStoreSizeLabel = new JLabel();
            storageStatePanel.addIn(carBodyStoreSizeLabel);

            carEngineStoreSizeLabel = new JLabel();
            storageStatePanel.addIn(carEngineStoreSizeLabel);

            carAccessoryStoreSizeLabel = new JLabel();
            storageStatePanel.addIn(carAccessoryStoreSizeLabel);
        }

        {
            var factoryStatePanel = new BorderedPanel("Factory state");
            mainPanel.add(factoryStatePanel, BorderLayout.SOUTH);

            carPriceLabel = new JLabel();
            factoryStatePanel.addIn(carPriceLabel);

            totalSoldLabel = new JLabel();
            factoryStatePanel.addIn(totalSoldLabel);

            buildStateLabel = new JLabel();
            factoryStatePanel.addIn(buildStateLabel);
        }

        var timer = new Timer(1000, (ActionEvent e) -> {
            carPriceLabel.setText("Car price: " + new DecimalFormat("#0.##").format(factory.getDealerCarPrice()));
            carBodyStoreSizeLabel.setText("Car body storage size: " + factory.getCarBodyStoreSize());
            carEngineStoreSizeLabel.setText("Car engine storage size: " + factory.getCarEngineStoreSize());
            carAccessoryStoreSizeLabel.setText("Car accessory storage size: " + factory.getCarAccessoryStoreSize());
            totalSoldLabel.setText("Total sold: " + factory.getTotalSold());
            buildStateLabel.setText("Build state: " + (factory.isBuildingPaused() ? "paused" : "running"));
            pack();
        });
        timer.start();

        pack();
        setPreferredSize(new Dimension(500, 500));
        setVisible(true);
    }

    void initStateLabels() {

    }
}
