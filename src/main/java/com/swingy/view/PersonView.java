package com.swingy.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.swingy.model.Hero;
import com.swingy.model.Villain;

public class PersonView extends JPanel {
    private Image image; // Image of the Hero or Villain
    private JLabel nameLabel;
    private JLabel expLabel;
    private JLabel statsLabel;
    private JLabel equipementLabel;
    private JLabel itemLabel;

    // Method to resize the image
    private BufferedImage resizeImage(Image originalImage, int targetWidth, int targetHeight) {
        if (originalImage != null) {
            BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            g2d.dispose();
            return resizedImage;
        }
        return null;
    }

    public Image loadImage(String path) {
        Image image = null;
        try {
            // Load the image from the specified file path
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println(path);
            e.printStackTrace(); // Handle the exception (e.g., file not found)
        }
        return image; // Return the loaded image
    }

    public PersonView(Hero hero, Boolean isBig) {
        Image rawImage = null;
        switch (hero.getHeroClass()) {
            case Knight:
                rawImage = this.loadImage("./asset/knight.jpeg");
                break;
            case Assassin:
                rawImage = this.loadImage("./asset/assassin.jpeg");
                break;
            case Barbarian:
                rawImage = this.loadImage("./asset/barbarian.jpeg");
                break;
            default:
                break;
        }
        if (isBig) {
            this.image = resizeImage(rawImage, 65, 90);
        } else {
            this.image = resizeImage(rawImage, 50, 50);
        }

        // Set layout for the PersonView
        setLayout(new BorderLayout());

        // Create a panel for the image
        JPanel imagePanel = new JPanel();
        if (isBig) {
            imagePanel.setPreferredSize(new Dimension(100, 100)); // Set preferred size for the image panel
        } else {
            imagePanel.setPreferredSize(new Dimension(50, 50)); // Set preferred size for the image panel
        }
        imagePanel.add(new JLabel(new ImageIcon(image))); // Add image as an icon to a JLabel

        add(imagePanel, BorderLayout.WEST); // Image on the left
        if (isBig) {

            // Create a panel for the stats
            JPanel statsPanel = new JPanel();
            statsPanel.setLayout(new GridLayout(5, 1)); // Vertical layout for stats

            // Create labels for each property
            nameLabel = new JLabel(String.format("%s(%s)", hero.getName(), hero.getHeroClass()));
            expLabel = new JLabel(String.format("Exp(lvl%d): %d", hero.getLevel(), hero.getExperience()));
            statsLabel = new JLabel(String.format("HP(%d/100) Att(%d) Def(%d)", hero.getHitPoints(), hero.getAttack(),
                    hero.getDefense()));

            // Add labels to the stats panel
            statsPanel.add(nameLabel);
            statsPanel.add(expLabel);
            statsPanel.add(statsLabel);
            String weapon = "";
            String armor = "";
            String helm = "";
            if (hero.getWeapon() != null) {
                weapon = String.format("Weapon(%d) ", hero.getWeapon().getIncreaseAttack());
            }
            if (hero.getArmor() != null) {
                armor = String.format("Armor(%d) ", hero.getArmor().getIncreaseDefense());
            }
            if (hero.getHelm() != null) {
                helm = String.format("Helm(%d) ", hero.getHelm().getIncreaseHitPoint());
            }
            String equipmentString = weapon + armor + helm;
            if (equipmentString.length() > 0) {
                equipementLabel = new JLabel(equipmentString);
                statsPanel.add(equipementLabel);
            }
            add(statsPanel, BorderLayout.CENTER); // Stats on the right
        }
    }

    public PersonView(Villain villain, Boolean isBig) {
        Image rawImage = null;
        switch (villain.getVillainClass()) {
            case normal:
                rawImage = this.loadImage("./asset/vNormal.jpeg");
                break;
            case elite:
                rawImage = this.loadImage("./asset/vElite.jpeg");
                break;
            case boss:
                rawImage = this.loadImage("./asset/vBoss.jpeg");
                break;
            default:
                break;
        }
        if (isBig) {
            this.image = resizeImage(rawImage, 65, 90);
        } else {
            this.image = resizeImage(rawImage, 50, 50);
        }

        // Set layout for the PersonView
        setLayout(new BorderLayout());

        // Create a panel for the image
        JPanel imagePanel = new JPanel();
        if (isBig) {
            imagePanel.setPreferredSize(new Dimension(100, 100)); // Set preferred size for the image panel
        } else {
            imagePanel.setPreferredSize(new Dimension(50, 50)); // Set preferred size for the image panel
        }
        imagePanel.add(new JLabel(new ImageIcon(image))); // Add image as an icon to a JLabel

        add(imagePanel, BorderLayout.WEST); // Image on the left
        if (isBig) {

            // Create a panel for the stats
            JPanel statsPanel = new JPanel();
            statsPanel.setLayout(new GridLayout(5, 1)); // Vertical layout for stats

            // Create labels for each property
            nameLabel = new JLabel(String.format("%s", villain.getVillainClass()));
            statsLabel = new JLabel(
                    String.format("HP(%d/100) Att(%d) Def(%d)", villain.getHitPoints(), villain.getAttack(),
                            villain.getDefense()));
            itemLabel = new JLabel(String.format("Item: %s", villain.getItemOwned()));

            // Add labels to the stats panel
            statsPanel.add(nameLabel);
            statsPanel.add(statsLabel);
            statsPanel.add(itemLabel);
            add(statsPanel, BorderLayout.CENTER); // Stats on the right
        }
    }

    // Method to update Hero properties
    public void updateHero(Hero hero) {
        nameLabel.setText(String.format("%s(%s)", hero.getName(), hero.getHeroClass()));
        expLabel.setText(String.format("Exp(lvl%d): %d", hero.getLevel(), hero.getExperience()));
        statsLabel.setText(String.format("HP(%d/100) Att(%d) Def(%d)", hero.getHitPoints(), hero.getAttack(),
                hero.getDefense()));
        String weapon = "";
        String armor = "";
        String helm = "";
        if (hero.getWeapon() != null) {
            weapon = String.format("Weapon(%d) ", hero.getWeapon().getIncreaseAttack());
        }
        if (hero.getArmor() != null) {
            armor = String.format("Armor(%d) ", hero.getArmor().getIncreaseDefense());
        }
        if (hero.getHelm() != null) {
            helm = String.format("Helm(%d) ", hero.getHelm().getIncreaseHitPoint());
        }
        String equipmentString = weapon + armor + helm;
        System.out.println("equipmentString: " + equipmentString);
        if (equipmentString.length() > 0) {
            equipementLabel.setText(equipmentString);
        }
    }

    // Method to update Villain properties
    public void updateVillain(Villain villain) {
        nameLabel.setText(String.format("%s", villain.getVillainClass()));
        statsLabel.setText(
                String.format("HP(%d/100) Att(%d) Def(%d)", villain.getHitPoints(), villain.getAttack(),
                        villain.getDefense()));
        itemLabel.setText(String.format("Item: ", villain.getItemOwned().toString()));
    }
}
