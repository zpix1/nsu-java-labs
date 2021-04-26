package ru.nsu.fit.ibaksheev.lab4.factory.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class BorderedPanel extends JPanel {
    Border border;
    JPanel innerPanel;

    public void addIn(Component comp) {
        innerPanel.add(comp);
    }

    public BorderedPanel(String title) {
        super();

        border = BorderFactory.createTitledBorder(title);

        setBorder(border);

        innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        add(innerPanel);
    }
}
