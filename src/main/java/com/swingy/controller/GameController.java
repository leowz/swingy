package com.swingy.controller;

import java.util.Scanner;
import java.lang.System;

import com.swingy.model.GameMap;
import com.swingy.model.Hero;
import com.swingy.model.HeroClass;

public class GameController {
    private GameMap map;
    private boolean gui;

    public GameController(boolean gui) {
        this.gui = gui;
    }

    public void start() {
        System.out.println("game starts");
        if (this.couldLoadGame()) {
            System.out.println("load game");
            this.loadGame();
        } else {
            System.out.println("start new game");
            this.startNew();
            System.out.println("new game started");
            System.out.println(this.map);
        }

        while (!this.isGameEnded()) {
            this.executeRound();
        }
    }

    public void startNew() {
        if (this.gui) {
            this.initGuiNewGame();
        } else {
            this.initNewGame();
        }
    }

    public void loadGame() {
        // if (this.gui) {
        // this.initGame();
        // } else if (!this.gui) {
        // this.initGuiGame();
        // }
    }

    public void executeRound() {

    }

    public boolean couldLoadGame() {
        return false;
    }

    public boolean isGameEnded() {
        return false;
    }

    public void startingText() {
        System.out.println("!-------------------------------------!");
        System.out.println("***********Hero Game*******************");
        System.out.println("!-------------------------------------!");
        System.out.println("");
        System.out.println("Hello, welcome to the game of the heros.");
        System.out.println("Here you are able to choose a hero and beat villains!");

        System.out.println("");
        System.out.println("");
        System.out.println("");
    }

    public Hero newHeroFromInput() {
        HeroClass[] heroClasses = HeroClass.values();
        Scanner scanner = new Scanner(System.in);
        System.out.println("There are " + heroClasses.length + " types of Heros. They are: ");

        for (int i = 0; i < heroClasses.length; i++) {
            System.out.println((i + 1) + ", " + heroClasses[i]);
        }
        System.out.println("Please choose the number of your hero.");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume the newline character left by nextInt()
        System.out.println("You have choosen: " + heroClasses[index]);
        if (index >= 0 && index < heroClasses.length) {
            Hero hero = new Hero(heroClasses[index]);
            System.out.println("Please input a name for your hero.");
            String name = scanner.nextLine();
            hero.setName(name);
            scanner.close();
            return hero;
        } else {
            scanner.close();
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
    }

    public void initNewGame() {
        startingText();
        Hero hero = newHeroFromInput();
        System.err.println("hero get: " + hero);
        this.map = new GameMap(hero);
    }

    public void initGuiNewGame() {
    }
}
