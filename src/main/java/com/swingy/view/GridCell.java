package com.swingy.view;

import javax.swing.*;
import java.awt.*;

public class GridCell extends JPanel {
    public GridCell() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border for visibility
        setPreferredSize(new Dimension(20, 20)); // Set a preferred size for consistent grid cell size
    }

    public void setContent(Component component) {
        removeAll(); // Clear previous content
        add(component, BorderLayout.CENTER); // Add new content
        revalidate(); // Refresh the panel
        repaint(); // Repaint the panel
    }
}