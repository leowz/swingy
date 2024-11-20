package com.swingy.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.swingy.model.Artifact;
import com.swingy.model.GameMap;
import com.swingy.model.Hero;
import com.swingy.model.Move;
import com.swingy.model.Point;
import com.swingy.view.GameView;
import com.swingy.view.MyView;
import com.swingy.view.StartingView;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validator;

public class GuiGameController extends GameController {
    public MyView view;
    public Boolean roundOnGoing = false;

    public GuiGameController() {
        super();
    }

    public MyView getView() {
        return view;
    }

    public void setView(MyView view) {
        this.view = view;
    }

    public void beforeGameStart() {
        // Dispose of the current view
        if (this.view != null) {
            this.view.getFrame().dispose(); // Close the StartingView
        }
        // Create and show the HeroCreationView
        GameView gameView = new GameView();
        gameView.initMap(this.map);
        gameView.initAction((Move move) -> {
            executeRound(move);
        }, (Boolean confirm) -> {
            System.out.println("Confirm to be  " + confirm);

        });
        this.view = gameView;
    }

    public void beforeGameExit() {
    }

    public void loadOrStartNewGame() {
        StartingView view = (StartingView) this.view;

        view.setCouldLoadGameView(this.couldLoadGame(),
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Load Game action executed.");
                        loadGame();
                    }
                }, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("New Game action executed.");
                        initNewGame(null);
                    }
                });
    }

    @Override
    public void start() {
        loadOrStartNewGame();
    }

    public void startGamingLoop() {
        beforeGameStart();
        try {
            System.out.println("game starts");
            // while (!this.isGameEnded()) {
            // this.executeRound();
            // }
        } catch (Error error) {
            beforeGameExit();
            throw error;
        }
        beforeGameExit();
    }

    public void escapeOrFight(Point nexPoint) {
        GameView view = (GameView) this.view;
        view.showDecisionModal("You just run into a Villain, would you like to fight or try to escape?", "Fight",
                "Escape",
                (Boolean wantEscape) -> {
                    GameView tempView = (GameView) this.view;
                    if (wantEscape) {
                        if (this.couldPlayerEscape()) {
                            tempView.showMessageModal("Escape Successfully, return to original place!");
                        } else {
                            tempView.showMessageModal("Failed to escape, start battle!");
                        }
                    } else {
                        // tempView.showModal("");
                        System.out.println("Battel started");
                        this.startBattle(nexPoint);
                    }
                });

    }

    public void moveHero(Point nextPoint) {
        this.map.moveHero(nextPoint);
        this.updateUiFromMap();
    }

    public void takeDroppedItemOrNot(Artifact droppedItem) {
        GameView gameView = (GameView) this.view;
        gameView.showDecisionModal(mapFileName, null, null, (Boolean confirm) -> {
        });
    }

    public void heroDiedGameEnd() {
        System.out.println("Do things after hero dies");
    }

    public void startBattle(Point nextPoint) {
        Artifact droppedItem = this.map.startBattle(nextPoint);
        if (this.map.isHeroAlive()) {
            this.takeDroppedItemOrNot(droppedItem);
            // if (droppedItem != null && this.isPlayerWantTheDroppedItem(droppedItem)) {
            // this.map.heroTakeDroppedItem(droppedItem);
            // }
            this.moveHero(nextPoint);
        } else {
            System.out.println("Hero dead, game end!");
            this.heroDiedGameEnd();
        }
    }

    public void updateUiFromMap() {
        GameView gameView = (GameView) this.view;
        gameView.updateMap();
    }

    public void executeRound(Move move) {
        System.out.println("Your next move is : " + move);
        if (roundOnGoing) {
            return;
        } else {
            this.roundOnGoing = true;
            Point nextPoint = this.map.nextPosition(move);
            System.out.println("next point " + nextPoint);
            // could move
            if (nextPoint != null) {
                if (this.map.wouldMeetVilain(nextPoint)) {
                    escapeOrFight(nextPoint);
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
                    this.moveHero(nextPoint);
                }
            } else {
                System.out.println("Your Hero wins!!! now go to next game!");
                this.initNextGame();
                this.map.displayHero();
            }
            this.roundOnGoing = false;
        }
    }

    public void initNewGame(String[] msgs) {
        StartingView view = (StartingView) this.view;
        view.setCreatHeroView(msgs, (heroType, heroName) -> {
            System.out.println("Hero Created: " + heroName + " of Type " + heroType);
            Hero hero = new Hero(heroType);
            hero.setName(heroName);
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Hero>> violations = validator.validate(hero);
            if (!violations.isEmpty()) {
                List<String> messages = new ArrayList<String>();
                for (ConstraintViolation<Hero> violation : violations) {
                    messages.add(violation.getMessage());
                }
                this.initNewGame(messages.toArray(new String[0]));
            } else {
                this.map = new GameMap(hero);
                this.startGamingLoop();
            }
        });
    }
}
