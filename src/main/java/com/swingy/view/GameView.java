package com.swingy.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; // Add this import
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.swingy.model.GameMap;
import com.swingy.model.Move;
import com.swingy.model.Person;

public class GameView extends MyView {
    private JPanel mapPanel;
    private JPanel controlPanel;
    private JPanel navPanel;
    private JButton upButton;
    private JButton downButton;
    private JButton leftButton;
    private JButton rightButton;
    private GameMap gameMap;
    private JDialog msgModalDialog;
    private JDialog decisionModalDialog;

    public GameView() {
        // Set layout for the main view
        frame.setSize(1000, 1300);
        frame.setLayout(new BorderLayout());

        // Create the map panel (80% height)
        mapPanel = new JPanel();
        mapPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mapPanel.setPreferredSize(new Dimension(this.frame.getWidth(), (int) (this.frame.getHeight() * 0.8)));
        mapPanel.setBackground(Color.LIGHT_GRAY); // Placeholder for the map
        frame.add(mapPanel, BorderLayout.CENTER);

        // Create the control panel (20% height)
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 3)); // 1 row, 3 columns

        // Create navigation buttons
        navPanel = new JPanel(new GridLayout(3, 3)); // 3 rows, 1 column
        upButton = new JButton("Up");
        downButton = new JButton("Down");
        leftButton = new JButton("Left");
        rightButton = new JButton("Right");
        // Create invisible buttons for spacing
        JButton emptyButton1 = new JButton();
        JButton emptyButton2 = new JButton();
        JButton emptyButton3 = new JButton();
        JButton emptyButton4 = new JButton();
        JButton emptyButton5 = new JButton();

        // Set empty buttons to be invisible and disabled
        emptyButton1.setVisible(false);
        emptyButton2.setVisible(false);
        emptyButton3.setVisible(false);
        emptyButton4.setVisible(false);
        emptyButton5.setVisible(false);

        emptyButton1.setEnabled(false);
        emptyButton2.setEnabled(false);
        emptyButton3.setEnabled(false);
        emptyButton4.setEnabled(false);
        emptyButton5.setEnabled(false);

        // Add buttons to the navPanel in the controller layout
        navPanel.add(emptyButton1); // Empty space
        navPanel.add(upButton); // Up button
        navPanel.add(emptyButton2); // Empty space
        navPanel.add(leftButton); // Left button
        navPanel.add(emptyButton3); // Empty space
        navPanel.add(rightButton); // Right button
        navPanel.add(emptyButton4); // Empty space
        navPanel.add(downButton); // Down button
        navPanel.add(emptyButton5); // Empty space

        // Add navigation and action panels to control panel
        controlPanel.add(navPanel);

        // Add control panel to the bottom
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void initMap(GameMap map) {
        gameMap = map; // Store the GameMap instance
        updateMap();
    }

    public void initAction(Consumer<Move> onClickDirection) {
        // Create an array of corresponding Move enum values
        Move[] moves = { Move.North, Move.West, Move.South, Move.East };
        JButton[] directionButtons = new JButton[] { upButton, leftButton, downButton, rightButton };

        // Add action listeners to the direction buttons using a for loop
        for (int i = 0; i < directionButtons.length; i++) {
            final Move move = moves[i]; // Capture the current move in a final variable
            directionButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onClickDirection.accept(move);
                }
            });
        }
    }

    public void updateStats() {
        System.out.println("update stats panel");
    }

    public void updateMap() {
        int size = gameMap.getSize(); // Get the size of the map
        mapPanel.removeAll(); // Clear previous components
        mapPanel.setLayout(new GridLayout(size, size)); // Set layout to grid

        // Create cells for each position in the map
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Person person = gameMap.getMap()[i][j]; // Get the Person at the current position
                GridCell cell = new GridCell(); // Create a new GridCell

                // Set the content of the cell
                if (person != null) {
                    JLabel label = new JLabel(person.toString().substring(0, 3)); // Display the Person's string
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    cell.setContent(label); // Set the label as the content of the cell
                } else {
                    cell.setContent(new JLabel()); // Empty cell
                }

                mapPanel.add(cell); // Add cell to the map panel
            }
        }

        mapPanel.revalidate(); // Refresh the panel
        mapPanel.repaint(); // Repaint the panel
    }

    public void showMessageModal(String message, Consumer<Void> onDismiss) {
        // Create a modal dialog
        msgModalDialog = new JDialog();
        msgModalDialog.setModal(true); // Set the dialog to be modal
        msgModalDialog.setTitle("Message");

        // Create a label to display the message
        JLabel messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        msgModalDialog.add(messageLabel, BorderLayout.CENTER);

        // Create a button to dismiss the modal
        JButton dismissButton = new JButton("Dismiss");
        dismissButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dismissMsg(); // Call dismissModal when the button is clicked
                if (onDismiss != null) {
                    onDismiss.accept(null);
                }
            }
        });
        msgModalDialog.add(dismissButton, BorderLayout.SOUTH);

        // Set the size and location of the dialog
        msgModalDialog.setSize(300, 150);
        msgModalDialog.setLocationRelativeTo(this.frame); // Center the dialog relative to the GameView
        msgModalDialog.setVisible(true); // Show the dialog
    }

    public void showDecisionModal(String message, String title, String titleYes, String titleNo,
            Consumer<Boolean> onClickEscape) {
        // Create a modal dialog
        decisionModalDialog = new JDialog();
        decisionModalDialog.setModal(true); // Set the dialog to be modal
        String modalTitle = title == null ? title : "Warning!";
        decisionModalDialog.setTitle(modalTitle);

        // Create a label to display the message
        JLabel messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        decisionModalDialog.add(messageLabel, BorderLayout.CENTER);

        // Create a button to dismiss the modal
        String t1 = titleYes == null ? "Yes" : titleYes;
        String t2 = titleNo == null ? "No" : titleNo;
        JButton yesButton = new JButton(t1);
        JButton noButton = new JButton(t2);
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dismissDecision(); // Call dismissModal when the button is clicked
                if (onClickEscape != null) {
                    onClickEscape.accept(true);
                }
            }
        });
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dismissDecision(); // Call dismissModal when the button is clicked
                if (onClickEscape != null) {
                    onClickEscape.accept(false);
                }
            }
        });
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(yesButton, BorderLayout.WEST); // Add yesButton to the left
        buttonPanel.add(noButton, BorderLayout.EAST); // Add noButton to the right
        decisionModalDialog.add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the dialog

        // Set the size and location of the dialog
        decisionModalDialog.setSize(500, 250);
        decisionModalDialog.setLocationRelativeTo(this.frame); // Center the dialog relative to the GameView
        decisionModalDialog.setVisible(true); // Show the dialog
    }

    public void dismissMsg() {
        if (msgModalDialog != null) {
            msgModalDialog.dispose(); // Close the modal dialog
            msgModalDialog = null; // Clear the reference
        }
    }

    public void dismissDecision() {
        if (decisionModalDialog != null) {
            decisionModalDialog.dispose(); // Close the modal dialog
            decisionModalDialog = null; // Clear the reference
        }
    }

    public void dismissAllModal() {
        dismissMsg();
        dismissDecision();
    }
}
