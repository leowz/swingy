package com.swingy.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.swingy.model.Hero;
import com.swingy.model.HeroClass;

import java.awt.Font; // Add this import
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color; // Add this import
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; // Add this import
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    public void setCouldLoadHeroView(Boolean couldLoadGame, Consumer<Void> onLoadGame,
            Consumer<Void> onNewGame, Consumer<Void> onExitGame) {
        frame.getContentPane().removeAll(); // Remove all components from the frame
        // Create buttons
        taskLabel = new JLabel("Welcom to the Hero Game!!!"); // Initialize JLabel
        taskLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font size and style
        taskLabel.setForeground(Color.RED); // Set text color
        taskLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
        // Input panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        gbc.anchor = GridBagConstraints.CENTER; // Center the label and buttons
        panel.add(taskLabel, gbc);
        gbc.gridy++;
        frame.add(panel, BorderLayout.CENTER);

        JButton loadGameButton = new JButton("Load Hero");
        JButton newGameButton = new JButton("New Hero");
        JButton exitGameButton = new JButton("Exit Game");

        // Set action listener for Load Game button from caller
        loadGameButton.addActionListener(e -> onLoadGame.accept(null));

        // Set action listener for New Game button from caller
        newGameButton.addActionListener(e -> onNewGame.accept(null));

        // Set action listener for New Game button from caller
        exitGameButton.addActionListener(e -> onExitGame.accept(null));

        gbc.gridy++; // Move to the next row for buttons
        panel.add(newGameButton, gbc); // Add New Game button
        gbc.gridy++; // Move to the next row for the next button
        if (couldLoadGame) {
            panel.add(loadGameButton, gbc); // Add Load Game button
        }
        gbc.gridy++; // Move to the next row for buttons
        panel.add(exitGameButton, gbc); // Add New Game button

        panel.revalidate(); // Refresh the panel to show new components
        panel.repaint(); // Repaint the panel
    }

    public void setLoadHeroView(Hero[] savedHeros, Consumer<Hero> onSelect, Consumer<Void> onBack) {
        // Clear existing components
        frame.getContentPane().removeAll(); // Remove all components from the frame

        // Create a panel for the hero selection
        JPanel heroPanel = new JPanel();
        heroPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center alignment with spacing

        // Assuming you have a method to get the list of heroes
        Hero[] heroes = savedHeros; // Replace with your method to fetch heroes

        for (Hero hero : heroes) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new PersonView(hero, true));
            JButton heroButton = new JButton("Select"); // Create button for each hero
            heroButton.addActionListener(e -> onSelect.accept(hero)); // Set action listener for selection
            panel.add(heroButton); // Add hero button to the panel
            heroPanel.add(panel);
        }

        // Create a scroll pane for horizontal scrolling
        JScrollPane scrollPane = new JScrollPane(heroPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(400, 100)); // Set preferred size for the scroll pane

        // Create a panel for the back button to center it
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center alignment
        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(e -> onBack.accept(null)); // Set action listener for back button
        backButtonPanel.add(backButton); // Add back button to the panel

        // Create a main panel to hold the scroll pane and back button
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Vertical layout
        mainPanel.add(scrollPane); // Add scroll pane to main panel
        mainPanel.add(backButtonPanel); // Add back button panel to main panel

        frame.add(mainPanel, BorderLayout.CENTER); // Add main panel to the frame
        frame.revalidate(); // Refresh the frame to show new components
        frame.repaint(); // Repaint the frame
    }

    public void setCreatHeroView(String[] msgs, BiConsumer<HeroClass, String> onConfirm, Consumer<Void> onBack) {
        // Clear existing components
        frame.getContentPane().removeAll(); // Remove all components from the frame

        // Create new components for hero creation
        String nameFieldText = "Enter Hero Name(3 - 10 letters)";
        JTextField heroNameField = new JTextField("");
        heroNameField.setText(nameFieldText);
        heroNameField.setForeground(Color.GRAY); // Set placeholder color
        heroNameField.setPreferredSize(new Dimension(200, 30)); // Set minimal width and height
        JButton confirmButton = new JButton("Confirm");
        JButton backButton = new JButton("Back");
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

        // Action listener for back button
        backButton.addActionListener(e -> onBack.accept(null));

        // Add new components to the frame
        JPanel newPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        if (msgs != null && msgs.length > 0) {
            gbc.insets = new Insets(10, 0, 10, 0);
            newPanel.add(warningLabel, gbc);
            gbc.gridy++;
        }
        gbc.insets = new Insets(10, 0, 10, 0);
        newPanel.add(heroTypeLabel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 10, 0);
        newPanel.add(heroNameField, gbc);
        gbc.gridy++;
        JPanel buttonsPanel = new JPanel(new FlowLayout()); // Use FlowLayout instead of GridBagLayout
        for (int i = 0; i < buttons.length; i++) {
            buttonsPanel.add(buttons[i], gbc);
        }
        gbc.insets = new Insets(10, 0, 10, 0);
        newPanel.add(buttonsPanel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 10, 0);
        newPanel.add(confirmButton, gbc);
        gbc.gridy++;
        newPanel.add(backButton, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 10, 0);
        newPanel.add(displayLabel, gbc);

        frame.add(newPanel, BorderLayout.CENTER);
        frame.revalidate(); // Refresh the frame to show new components
        frame.repaint(); // Repaint the frame
    }
}
