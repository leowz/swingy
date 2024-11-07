package com.swingy.controller;

import java.util.Scanner;
import java.lang.System;

import com.swingy.model.Artifact;
import com.swingy.model.GameMap;
import com.swingy.model.Hero;
import com.swingy.model.HeroClass;
import com.swingy.model.Move;
import com.swingy.model.Point;
import com.swingy.model.Utils;

public class GameController {
    private GameMap map;
    private boolean gui;
    private Scanner scanner;

    public GameController(boolean gui) {
        this.gui = gui;
        if (!this.gui) {
            scanner = new Scanner(System.in);
        }
    }

    public void start() {
        try {
            System.out.println("game starts");
            if (this.couldLoadGame()) {
                System.out.println("load game");
                this.loadGame();
            } else {
                System.out.println("start new game");
                this.startNew();
                System.out.println("new game started");
            }

            while (!this.isGameEnded()) {
                this.executeRound();
            }
        } catch (Error error) {
            beforeGameExit();
            throw error;
        }
        beforeGameExit();
    }

    public void startNew() {
        if (this.gui) {
            this.initGuiNewGame();
        } else {
            this.initNewGame();
        }
    }

    public void beforeGameExit() {
        if (this.gui) {
            this.closeScanner();
        }
    }

    public void loadGame() {
        // if (this.gui) {
        // this.initGame();
        // } else if (!this.gui) {
        // this.initGuiGame();
        // }
    }

    public boolean isPlayerWantEscape() {
        System.out.println(
                "You encountered a Vaillan, could you fight the villain or try to Run to previous location?");
        System.out.println("1. Fight");
        System.out.println("2. Run (to previous location)");
        System.out.println("Please input the number of your decision");
        int decision = this.scanner.nextInt();
        if (decision == 2)
            return true;
        return false;
    }

    public boolean couldPlayerEscape() {
        int chance = Utils.getRandomNumber(0, 100);
        if (chance > 50) {
            System.out.println("Escap successfully!");
            return true;
        } else {
            System.out.println("Escap failed, you have to fight the battle!");
            return false;
        }
    }

    public boolean isPlayerWantTheDroppedItem(Artifact item) {
        System.out.println();
        System.out.println("Villan Dropped the item: \n" + item);
        this.map.showHeroItems();
        System.out.println();
        System.out.println("Do you want equipe this new equipement? Old one will be replaced.");
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.println("Please input the number of your decision");
        int decision = this.scanner.nextInt();
        if (decision == 1)
            return true;
        return false;
    }

    public void initNextGame() {
        Hero hero = this.map.heroForNextGame();
        this.map = new GameMap(hero);
    }

    public void executeRound() {
        System.out.println(this.map);
        Move nextMove = nextMoveInput();
        System.out.println("Your next move is : " + nextMove);
        if (nextMove != null) {
            Point nextPoint = this.map.nextPosition(nextMove);
            System.out.println("next point " + nextPoint);
            // could move
            if (nextPoint != null) {
                if (this.map.wouldMeetVilain(nextPoint)) {
                    if (this.isPlayerWantEscape() && this.couldPlayerEscape()) {
                        // go to previous position;
                        System.out.println("Run back to previous position.");
                    } else {
                        Artifact droppedItem = this.map.startBattle(nextPoint);
                        if (this.map.isHeroAlive()) {
                            if (droppedItem != null && this.isPlayerWantTheDroppedItem(droppedItem)) {
                                this.map.heroTakeDroppedItem(droppedItem);
                            }
                            this.map.moveHero(nextPoint);
                        } else {
                            System.out.println("Hero dead, game end!");
                        }
                    }
                } else {
                    this.map.moveHero(nextPoint);
                }
            } else {
                System.out.println();
                System.out.println("Your Hero wins!!! now go to next game!");
                this.initNextGame();
                System.out.println();
                System.out.println("Game map: ");
                System.out.println(this.map);
                this.map.displayHero();
                System.out.println();
            }
        }
    }

    public boolean couldLoadGame() {
        return false;
    }

    public boolean isGameEnded() {
        boolean heroAlive = this.map.isHeroAlive();
        return !heroAlive;
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
        System.out.println("There are " + heroClasses.length + " types of Heros. They are: ");

        for (int i = 0; i < heroClasses.length; i++) {
            System.out.println((i + 1) + ", " + heroClasses[i]);
        }
        System.out.println("Please choose the number of your hero.");
        int index = this.scanner.nextInt() - 1;
        this.scanner.nextLine(); // Consume the newline character left by nextInt()
        System.out.println("You have choosen: " + heroClasses[index]);
        if (index >= 0 && index < heroClasses.length) {
            Hero hero = new Hero(heroClasses[index]);
            System.out.println("Please input a name for your hero.");
            String name = this.scanner.nextLine();
            hero.setName(name);
            return hero;
        } else {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
    }

    public Move nextMoveInput() {
        Move[] moves = Move.values();
        System.out.println("Please take your next move");

        for (int i = 0; i < moves.length; i++) { // Updated to use moves array
            System.out.println((i + 1) + ". " + moves[i]); // Changed to print moves
        }
        int index = this.scanner.nextInt() - 1;
        this.scanner.nextLine();
        if (index >= 0 && index < moves.length) {
            return moves[index];
        } else {
            System.err.println("Please input a valid number shown on screen");
            return null;
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

    public void closeScanner() {
        if (this.scanner != null) {
            this.scanner.close(); // Close when done
        }
    }
}
