package com.swingy.controller;

import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.lang.System;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swingy.model.Artifact;
import com.swingy.model.GameMap;
import com.swingy.model.Hero;
import com.swingy.model.HeroClass;
import com.swingy.model.Move;
import com.swingy.model.Point;
import com.swingy.model.Utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class GameController {
    public GameMap map;
    public Scanner scanner;
    public String mapFileName = "map.json";
    public ObjectMapper om = new ObjectMapper();
    public File mapFile = new File(mapFileName);

    public GameController() {
    }

    public void beforeGameStart() {
        scanner = new Scanner(System.in);
    }

    public void start() {
        beforeGameStart();
        try {
            System.out.println("game starts");
            if (this.shouldLoadTheGameIfExist()) {
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
        this.initNewGame();
    }

    public void beforeGameExit() {
        this.closeScanner();
    }

    public boolean shouldLoadTheGameIfExist() {
        if (this.couldLoadGame()) {
            System.out.println("1. Start New Game");
            System.out.println("2. Load Saved Game");
            System.out.println("Please input the number of your decision");
            int decision = this.scanner.nextInt();
            if (decision == 2)
                return true;
            return false;
        }
        return false;
    }

    public void loadGame() {
        if (mapFile.exists()) {
            try {
                GameMap loadedMap = om.readValue(mapFile, GameMap.class);
                this.map = loadedMap;
                mapFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveGame() {
        System.out.println("saving game before exit");
        try {
            System.out.println("serilise Map");
            om.writeValue(mapFile, map);
            System.out.println("User object saved to user.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return mapFile.exists();
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
        Hero hero = null;
        boolean success = false;
        HeroClass[] heroClasses = HeroClass.values();

        while (!success) {
            try {
                System.out.println("There are " + heroClasses.length + " types of Heros. They are: ");
                for (int i = 0; i < heroClasses.length; i++) {
                    System.out.println((i + 1) + ", " + heroClasses[i]);
                }
                System.out.println("Please choose the number of your hero.");
                int index = this.scanner.nextInt() - 1;
                this.scanner.nextLine(); // Consume the newline character left by nextInt()
                System.out.println("You have choosen: " + heroClasses[index]);
                hero = new Hero(heroClasses[index]);
                System.out.println("Please input a name for your hero.(3-10 letters)");
                String name = this.scanner.nextLine();
                hero.setName(name);
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();
                Set<ConstraintViolation<Hero>> violations = validator.validate(hero);
                if (!violations.isEmpty()) {
                    for (ConstraintViolation<Hero> violation : violations) {
                        throw new Error(violation.getMessage());
                    }
                }
                success = true;
            } catch (Error e) {
                String message = e.getMessage();
                System.out.println("Error in inputing hero info");
                System.out.println(message);
                System.out.println("Please put in correct details");
                success = false;
            }
        }
        return hero;
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
