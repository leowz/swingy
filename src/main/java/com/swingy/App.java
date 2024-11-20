package com.swingy;

import javax.swing.SwingUtilities;

import com.swingy.controller.GameController;
import com.swingy.controller.GuiGameController;
import com.swingy.view.StartingView;

public class App {
    public static void main(String[] args) {
        GameController controller;

        if (args.length < 1 || args[0].equals("gui")) {
            controller = new GuiGameController();
        } else if (args[0].equals("console")) {
            System.err.println("console game");
            controller = new GameController();
        } else {
            System.err.println("Argument not recognized.");
            return;
        }
        // Register a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook triggered. Cleaning up before exit...");
            performCleanup(controller);
            System.out.println("Cleanup completed. Application exiting.");
        }));
        try {
            if (controller instanceof GuiGameController) {
                GuiGameController uc = (GuiGameController) controller;
                // start in another thread
                SwingUtilities.invokeLater(() -> {
                    StartingView view = new StartingView();
                    uc.setView(view);
                    uc.start();
                });
            } else {
                controller.start();
            }
        } catch (Error e) {
            System.out.println(e.getMessage());
        }
    }

    private static void performCleanup(GameController controller) {
        controller.saveGame();
        System.out.println("Performing necessary cleanup...");
    }
}
