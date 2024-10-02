package com.swingy;

import com.swingy.controller.GameController;

public class App {
    public static void main(String[] args) {
        GameController game;
        if (args.length < 1 || args[0].equals("gui")) {
            System.err.println("gui game");
            game = new GameController(true);
        } else if (args[0].equals("console")) {
            System.err.println("console game");
            game = new GameController(false);
        } else {
            System.err.println("Argument not recognized.");
            return;
        }
        game.start();
    }
}
