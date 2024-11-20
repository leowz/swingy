package com.swingy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    public GameMap() {
    }

    public void setMap(Person[][] map) {
        this.map = map;
    }

    public Person[][] getMap() {
        return map;
    }

    public void setVillains(Villain[] villains) {
        this.villains = villains;
    }

    public Villain[] getVillains() {
        return villains;
    }

    public void setHeroPosition(Point heroPosition) {
        this.heroPosition = heroPosition;
    }

    public Point getHeroPosition() {
        return heroPosition;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

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

    @JsonIgnore
    public boolean isHeroAlive() {
        return this.hero.isAlive();
    }

    public void displayHero() {
        System.out.println("hero: ");
        System.out.println(this.hero);
    }

    public Hero heroForNextGame() {
        if (hero.hitPoints < 100) {
            hero.upgradeStats(100, 0, 0);
        }
        return this.hero;
    }

    public void initVillain() {
        for (int i = 0; i < this.villains.length; i++) {
            Point pos = generateRandomPositionOnMap();
            if (pos != null && (this.map[pos.getX()][pos.getY()] == null)) {
                this.map[pos.getX()][pos.getY()] = this.villains[i];
            }
        }
    }

    public Point moveNorth() {
        int x = this.heroPosition.getX();
        if (x - 1 < 0)
            return null;
        else {
            return new Point(x - 1, this.heroPosition.getY());
        }
    }

    public Point moveSouth() {
        int x = this.heroPosition.getX();
        if (x + 1 >= this.size)
            return null;
        else {
            return new Point(x + 1, this.heroPosition.getY());
        }
    }

    public Point moveEast() {
        int y = this.heroPosition.getY();
        if (y + 1 >= this.size)
            return null;
        else {
            return new Point(this.heroPosition.getX(), y + 1);
        }
    }

    public Point moveWest() {
        int y = this.heroPosition.getY();
        if (y - 1 < 0)
            return null;
        else {
            return new Point(this.heroPosition.getX(), y - 1);
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

    public void heroTakeDroppedItem(Artifact item) {
        this.hero.equipeAritifact(item);
        System.out.println("Your hero with new items:");
        showHeroItems();
    }

    public void showHeroItems() {
        this.hero.showItems();
    }

    public Artifact startBattle(Point villainPoint) {
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
                    System.out.println("Hero wins the battle!");
                    hero.gainExperience(currentVillain.experience);
                    return currentVillain.getItemOwned();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;
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
