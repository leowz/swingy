package com.swingy;

import com.swingy.controller.GameController;

public class App {
    public static void main(String[] args) {

        // Register a shutdown hook
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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook triggered. Cleaning up before exit...");
            performCleanup(game);
            System.out.println("Cleanup completed. Application exiting.");
        }));
        try {
            game.start();
        } catch (Error e) {
            System.out.println(e.getMessage());
        }
    }

    // Cleanup code to execute during shutdown
    private static void performCleanup(GameController game) {
        // Add any cleanup code here, like saving files, closing resources, etc.
        game.saveGame();
        System.out.println("Performing necessary cleanup...");
        // e.g., save data to a file, close database connections, etc.
    }
}
