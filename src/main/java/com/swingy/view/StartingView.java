package com.swingy.view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.swingy.model.HeroClass;

import java.awt.Font; // Add this import
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color; // Add this import
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; // Add this import
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import java.util.function.BiConsumer;

public class StartingView extends MyView {
    private JTextField taskField;
    private JButton addButton;
    private JButton removeButton;
    private JList<String> taskList;
    private JLabel taskLabel;
    HeroClass selectedHero = null;
    String selectedHeroName = "";

    public StartingView() {
        super();
        frame.setLayout(new BorderLayout());
        taskLabel = new JLabel("Welcom to the Hero Game!!!"); // Initialize JLabel
        taskLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font size and style
        taskLabel.setForeground(Color.RED); // Set text color
        taskLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
        // Input panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(taskLabel, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public JTextField getTaskField() {
        return taskField;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public JList<String> getTaskList() {
        return taskList;
    }

    public void setCouldLoadGameView(Boolean couldLoadGame, ActionListener loadGameAction,
            ActionListener newGameAction) {
        // Create buttons
        JButton loadGameButton = new JButton("Load Game");
        JButton newGameButton = new JButton("New Game");

        // Set action listener for Load Game button from caller
        loadGameButton.addActionListener(loadGameAction);

        // Set action listener for New Game button from caller
        newGameButton.addActionListener(newGameAction);

        // Get the existing panel and set its layout to GridBagLayout
        JPanel panel = (JPanel) frame.getContentPane().getComponent(0); // Get the existing panel
        panel.setLayout(new GridBagLayout()); // Set GridBagLayout for centering

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        gbc.anchor = GridBagConstraints.CENTER; // Center the label and buttons
        panel.add(taskLabel, gbc); // Assuming taskLabel is your existing label

        gbc.gridy = 1; // Move to the next row for buttons
        panel.add(newGameButton, gbc); // Add New Game button
        gbc.gridy = 2; // Move to the next row for the next button
        if (couldLoadGame) {
            panel.add(loadGameButton, gbc); // Add Load Game button
        }

        panel.revalidate(); // Refresh the panel to show new components
        panel.repaint(); // Repaint the panel
    }

    public void setCreatHeroView(String[] msgs, BiConsumer<HeroClass, String> onConfirm) {
        // Clear existing components
        frame.getContentPane().removeAll(); // Remove all components from the frame

        // Create new components for hero creation
        String nameFieldText = "Enter Hero Name(3 - 10 letters)";
        JTextField heroNameField = new JTextField("");
        heroNameField.setText(nameFieldText);
        heroNameField.setForeground(Color.GRAY); // Set placeholder color
        heroNameField.setPreferredSize(new Dimension(200, 30)); // Set minimal width and height
        JButton confirmButton = new JButton("Confirm");
        JLabel displayLabel = new JLabel("", JLabel.CENTER);
        JLabel warningLabel = new JLabel("", JLabel.CENTER);
        JLabel heroTypeLabel = new JLabel("Choose Hero Type:");
        HeroClass[] heroClasses = HeroClass.values();
        JButton[] buttons = new JButton[heroClasses.length];

        if (msgs != null && msgs.length > 0) {
            warningLabel.setText(String.join(" ", msgs));
            warningLabel.setForeground(Color.red);
        }

        confirmButton.setEnabled(false);
        // Add a listener to the heroNameField to check conditions
        heroNameField.addCaretListener(e -> {
            String heroName = heroNameField.getText();
            confirmButton.setEnabled(selectedHero != null && !heroName.isEmpty());
        });
        heroNameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (heroNameField.getText().equals(nameFieldText)) {
                    heroNameField.setText("");
                    heroNameField.setForeground(Color.BLACK); // Reset text color
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (heroNameField.getText().isEmpty()) {
                    heroNameField.setForeground(Color.GRAY); // Set placeholder color
                    heroNameField.setText(nameFieldText);
                }
            }
        });
        for (int i = 0; i < heroClasses.length; i++) {
            final int index = i;
            buttons[i] = new JButton(heroClasses[i].toString());
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    displayLabel.setText("Selected Hero: " + heroClasses[index].toString());
                    selectedHero = heroClasses[index];
                    String heroName = heroNameField.getText();
                    confirmButton.setEnabled(selectedHero != null && !heroName.isEmpty());
                }
            });
        }

        // Action listener for confirm button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String heroName = heroNameField.getText();
                if (selectedHero != null && !heroName.isEmpty()) {
                    displayLabel.setText("Confirmed: " + heroName + " (" + selectedHero + ")");
                    onConfirm.accept(selectedHero, heroName);
                } else {
                    displayLabel.setText("Please select a hero type and enter a name.");
                }
            }
        });

        // Add new components to the frame
        JPanel newPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        if (msgs != null && msgs.length > 0) {
            newPanel.add(warningLabel, gbc);
            gbc.gridy++;
        }
        newPanel.add(heroTypeLabel, gbc);
        gbc.gridy++;
        for (int i = 0; i < buttons.length; i++) {
            newPanel.add(buttons[i], gbc);
            gbc.gridy++;
        }
        newPanel.add(heroNameField, gbc);
        gbc.gridy++;
        newPanel.add(confirmButton, gbc);
        gbc.gridy++;
        newPanel.add(displayLabel, gbc);

        frame.add(newPanel, BorderLayout.CENTER);
        frame.revalidate(); // Refresh the frame to show new components
        frame.repaint(); // Repaint the frame
    }
}
