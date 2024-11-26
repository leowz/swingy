package com.swingy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.swingy.model.Artifact;
import com.swingy.model.GameMap;
import com.swingy.model.Hero;
import com.swingy.model.Move;
import com.swingy.model.Point;
import com.swingy.model.Villain;
import com.swingy.view.GameView;
import com.swingy.view.MyView;
import com.swingy.view.StartingView;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validator;

public class GuiGameController extends GameController {
    public MyView view;

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
            System.out.println("Move: " + move);
            executeRound(move);
        });
        this.view = gameView;
    }

    public void beforeGameExit() {
    }

    public void showLoadHero() {
        StartingView view = (StartingView) this.view;
        view.setLoadHeroView(this.savedHeros, (Hero hero) -> {
            this.map = new GameMap(hero);
            this.startGamingLoop();
        }, (Void) -> {
            this.loadOrStartNewHero();
        });
    }

    public void loadOrStartNewHero() {
        StartingView view = (StartingView) this.view;
        loadHero();
        view.setCouldLoadHeroView(this.couldLoadHeros(), (Void) -> {
            showLoadHero();
        }, (Void) -> {
            initNewGame(null);
        }, (Void) -> {
            System.exit(0);
        });
    }

    @Override
    public void start() {
        loadOrStartNewHero();
    }

    public void startGamingLoop() {
        beforeGameStart();
        try {
            System.out.println("game starts");
        } catch (Error error) {
            beforeGameExit();
            throw error;
        }
        beforeGameExit();
    }

    public void escapeOrFight(Point nexPoint) {
        GameView view = (GameView) this.view;
        Villain v = (Villain) this.map.getPersonByPoint(nexPoint);

        view.initVillainStat(v);
        view.showDecisionModal("You just run into a Villain, would you like to fight or try to escape?", null, "Fight",
                "Escape",
                (Boolean wantFight) -> {
                    GameView tempView = (GameView) this.view;
                    Boolean couldEscape = this.couldPlayerEscape();
                    if (!wantFight && couldEscape) {
                        tempView.showMessageModal("Escape Successfully, return to original place!", (Void) -> {
                            view.removeVillainStat();
                        });
                    } else {
                        if (!wantFight && !couldEscape) {
                            tempView.showMessageModal("Failed to escape, start battle!", (Void) -> {
                                System.out.println("Battel started");
                                this.fightBattle(nexPoint);
                            });
                        } else {
                            System.out.println("Battel started");
                            this.fightBattle(nexPoint);
                        }
                    }
                });
    }

    public void moveHero(Point nextPoint) {
        this.map.moveHero(nextPoint);
        this.updateUiFromMap();
    }

    public void heroDiedGameEnd() {
        System.out.println("Do things after hero dies");
        GameView gameView = (GameView) this.view;
        gameView.dismissGameView();
        StartingView view = new StartingView();
        setView(view);
        start();
    }

    public void updateFightAnimationAndStats(Consumer<Boolean> onFinish) {
        System.out.println("animation updates");
        onFinish.accept(true);
    }

    public void fightBattle(Point nextPoint) {
        GameView gameView = (GameView) this.view;

        Artifact droppedItem = this.map.doBattleRound(nextPoint);
        this.updateFightAnimationAndStats((Boolean fi) -> {
            if (this.map.isHeroAlive() && this.map.isPersonAlive(nextPoint)) {
                // fight not over;
                System.out.println("continue fight");
                this.fightBattle(nextPoint);
            } else if (this.map.isHeroAlive() && !this.map.isPersonAlive(nextPoint)) {
                // hero wins;
                System.out.println("hero wins");
                gameView.updateStats(this.map.getHero(), this.map.getVillainByPoint(nextPoint));
                if (droppedItem != null) {
                    gameView.showMessageModal("Your Hero wins!", (Void) -> {
                        this.map.displayHero();
                        String msg = "Do you want to take the dropped Item \n" + droppedItem.toString();
                        gameView.showDecisionModal(msg, "info", null, null, (Boolean confirm) -> {
                            gameView.removeVillainStat();
                            if (confirm) {
                                this.map.heroTakeDroppedItem(droppedItem);
                            }
                            gameView.updateStats(this.map.getHero(), null);
                        });

                    });
                }
                this.moveHero(nextPoint);
            } else {
                // hero deads;
                gameView.showMessageModal("!!! Your Hero is dead, GAME OVER !!!", (Void) -> {
                    System.out.println("Hero dead, game end!");
                    this.heroDiedGameEnd();
                });
            }
        });
    }

    public void updateUiFromMap() {
        GameView gameView = (GameView) this.view;
        gameView.updateMap();
    }

    public void executeRound(Move move) {
        System.out.println("Your next move is : " + move);

        Point nextPoint = this.map.nextPosition(move);
        System.out.println("next point " + nextPoint);
        // could move
        if (nextPoint != null) {
            if (this.map.wouldMeetVilain(nextPoint)) {
                escapeOrFight(nextPoint);
            } else {
                this.moveHero(nextPoint);
            }
        } else {
            System.out.println("Your Hero wins!\n now go to next game!");
            GameView gameView = (GameView) this.view;
            gameView.showMessageModal("Your Hero wins this round, now move to next round.", (Void) -> {
                this.initNextGame();
                startGamingLoop();
            });
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
                saveHero(hero);
                this.map = new GameMap(hero);
                this.startGamingLoop();
            }
        }, (Void) -> {
            this.loadOrStartNewHero();
        });
    }
}
