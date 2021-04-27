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

        mainPanel.setLayout(new GridBagLayout());

        var gbc = new GridBagConstraints();

        getContentPane().add(mainPanel);

        {
            var storageStatePanel = new BorderedPanel("Storage state");
            gbc.gridx = 0;
            gbc.gridy = 0;
            mainPanel.add(storageStatePanel, gbc);

            carBodyStoreSizeLabel = new JLabel();
            storageStatePanel.addIn(carBodyStoreSizeLabel);

            carEngineStoreSizeLabel = new JLabel();
            storageStatePanel.addIn(carEngineStoreSizeLabel);

            carAccessoryStoreSizeLabel = new JLabel();
            storageStatePanel.addIn(carAccessoryStoreSizeLabel);
        }

        {
            var factoryStatePanel = new BorderedPanel("Factory state");
            gbc.gridx = 1;
            gbc.gridy = 0;
            mainPanel.add(factoryStatePanel, gbc);

            carPriceLabel = new JLabel();
            factoryStatePanel.addIn(carPriceLabel);

            totalSoldLabel = new JLabel();
            factoryStatePanel.addIn(totalSoldLabel);

            buildStateLabel = new JLabel();
            factoryStatePanel.addIn(buildStateLabel);
        }

        {
            var factoryControlPanel = new BorderedPanel("Factory controls");
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 1;
            mainPanel.add(factoryControlPanel, gbc);
        }

        var timer = new Timer(200, (ActionEvent e) -> {
            updateInformation();
        });
        timer.start();

        updateInformation();
        pack();
        setPreferredSize(new Dimension(500, 500));
        setVisible(true);
    }

    private void updateInformation() {
        carPriceLabel.setText("Car price: " + new DecimalFormat("#0.##").format(factory.getDealerCarPrice()));
        carBodyStoreSizeLabel.setText("Car body storage size: " + factory.getCarBodyStoreSize() + " / " + factory.getCarBodyStoreCapacity());
        carEngineStoreSizeLabel.setText("Car engine storage size: " + factory.getCarEngineStoreSize() + " / " + factory.getCarEngineStoreCapacity());
        carAccessoryStoreSizeLabel.setText("Car accessory storage size: " + factory.getCarAccessoryStoreSize() + " / " + factory.getCarAccessoryStoreCapacity());
        totalSoldLabel.setText("Total sold: " + factory.getTotalSold());
        buildStateLabel.setText("Build state: " + (factory.isBuildingPaused() ? "paused" : "running"));
    }


    void initStateLabels() {

    }
}
