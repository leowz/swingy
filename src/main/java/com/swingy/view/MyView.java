package com.swingy.view;

import javax.swing.JFrame;

public class MyView {
    protected JFrame frame;

    public MyView() {
        // Initialize components
        frame = new JFrame("");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
    }

    // Getters for UI components
    public JFrame getFrame() {
        return frame;
    }
}
