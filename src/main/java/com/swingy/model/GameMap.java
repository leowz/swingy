package com.swingy.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GameMap {
    @NotNull(message = "Map should not be null")
    private Person[][] map;

    @NotNull(message = "villains should not be null")
    private Villain[] villains;

    @NotNull(message = "hero should not be null")
    private Hero hero;

    @NotNull(message = "hero position should not be null")
    private Point heroPosition;

    @NotNull(message = "size should not be null")
    @Size(min = 1, max = 100, message = "Size should be between 1 and 100")
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

    public boolean isHeroAlive() {
        return this.hero.isAlive();
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

    public Point moveNorth() {
        int y = this.heroPosition.getY();
        if (y - 1 < 0)
            return null;
        else {
            return new Point(this.heroPosition.getX(), y - 1);
        }
    }

    public Point moveSouth() {
        int y = this.heroPosition.getY();
        if (y + 1 >= this.size)
            return null;
        else {
            return new Point(this.heroPosition.getX(), y + 1);
        }
    }

    public Point moveEast() {
        int x = this.heroPosition.getX();
        if (x >= this.size)
            return null;
        else {
            return new Point(x + 1, this.heroPosition.getY());
        }
    }

    public Point moveWest() {
        int x = this.heroPosition.getX();
        if (x - 1 < 0)
            return null;
        else {
            return new Point(x - 1, this.heroPosition.getY());
        }
    }

    public boolean wouldMeetVilain(Point point) {
        Person p = this.map[point.getX()][point.getY()];
        System.out.println("Next point item " + p);
        if (p != null && p instanceof Villain)
            return true;
        else {
            return false;
        }
    }

    public void startBattle(Point villainPoint) {
        Villain currentVillain = (Villain) this.map[villainPoint.getX()][villainPoint.getY()];
        int roundCounter = 0;
        System.out.println();
        System.out.println();
        System.out.println("Battle started!");
        if (currentVillain != null) {
            try {
                while (currentVillain.isAlive() && hero.isAlive()) {
                    // hero attack
                    Thread.sleep(1000);
                    roundCounter++;
                    System.out.println("!-----------------Round(" + roundCounter + ") Start-----------------!");
                    System.out.println("Hero Attack Villain: ");
                    System.out.println(hero);
                    System.out.println(currentVillain);
                    currentVillain.takeDamage(hero.makeAttack());
                    System.out.println();
                    // villain attack
                    if (!currentVillain.isAlive()) {
                        System.out.println("!-----------------Round(" + roundCounter + ") End-----------------!");
                        break;
                    }
                    System.out.println("Villain Attack Hero: ");
                    System.out.println(hero);
                    System.out.println(currentVillain);
                    System.out.println();
                    hero.takeDamage(currentVillain.makeAttack());
                    System.out.println("!-----------------Round(" + roundCounter + ") End-----------------!");
                    System.err.println("");
                    Thread.sleep(3000);
                    System.err.println("");
                }
                if (hero.isAlive()) {
                    // after batter, herso still alive. hero gets exp
                    System.out.println("Hero wins the battle!");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Point nextPosition(Move nextMove) {
        Point nextPositiPoint = null;
        switch (nextMove) {
            case North:
                nextPositiPoint = this.moveNorth();
                break;
            case South:
                nextPositiPoint = this.moveSouth();
                break;
            case East:
                nextPositiPoint = this.moveEast();
                break;
            case West:
                nextPositiPoint = this.moveWest();
                break;
            default:
                break;
        }
        return nextPositiPoint;
    }

    public void moveHero(Point nextMovePoint) {
        this.map[heroPosition.getX()][heroPosition.getY()] = null;
        this.map[nextMovePoint.getX()][nextMovePoint.getY()] = hero;
        heroPosition = nextMovePoint;
    }

    public String toString() {
        String message = String.format("Map size(%d) with Herso(%s) at position (%d, %d): \n", this.size,
                this.hero.getName(), heroPosition.getX(), heroPosition.getY());
        message += String.format("!-------------------------------------!\n");
        for (int y = 0; y < this.size; y++) {
            message += String.format("    ");
            for (int x = 0; x < this.size; x++) {
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
