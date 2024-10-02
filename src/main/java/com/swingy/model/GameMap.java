package com.swingy.model;

public class GameMap {
    private Person[][] map;
    private Villain[] villains;
    private Hero hero;
    private Point heroPosition;
    private int size;

    public GameMap(Hero hero) {
        int size = (hero.level - 1) * 5 + 10 - (hero.level % 2);
        this.map = new Person[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.map[i][j] = null;
            }
        }
        int villainNbr = size * 2 + 1;
        this.villains = Villain.villainsFactory(villainNbr);
        this.hero = hero;
        this.size = size;
        this.heroPosition = new Point(size / 2, size / 2);
        this.map[heroPosition.getX()][heroPosition.getY()] = hero;
        this.initVillain();
    }

    public Point generateRandomPositionOnMap() {
        boolean found = false;
        int x, y;
        while (!found) {
            x = Utils.getRandomNumber(0, this.size);
            y = Utils.getRandomNumber(0, this.size);
            if (this.map[x][y] == null) {
                return new Point(x, y);
            }
        }
        return null;
    }

    public void initVillain() {
        for (int i = 0; i < this.villains.length; i++) {
            Point pos = generateRandomPositionOnMap();
            // System.out.println("position: " + pos + " " +
            // this.map[pos.getX()][pos.getY()]);
            if (pos != null && (this.map[pos.getX()][pos.getY()] == null)) {
                this.map[pos.getX()][pos.getY()] = this.villains[i];
                // System.out.println("after insert position: " + pos + " " +
                // this.map[pos.getX()][pos.getY()]);
            }
        }
    }

    public String toString() {
        String message = String.format("Map size(%d) with Herso(%s) at position (%d, %d): \n", this.size,
                this.hero.getName(), heroPosition.getX(), heroPosition.getY());
        message += String.format("!-------------------------------------!\n");
        for (int x = 0; x < this.size; x++) {
            message += String.format("    ");
            for (int y = 0; y < this.size; y++) {
                char c = '.';
                if (this.map[x][y] instanceof Villain) {
                    c = 'v';
                } else if (this.map[x][y] instanceof Hero) {
                    c = 'h';
                }
                message += String.format("%c ", c);
            }
            message += String.format("    \n");
        }
        message += String.format("!-------------------------------------!\n");
        return message;
    }
}
