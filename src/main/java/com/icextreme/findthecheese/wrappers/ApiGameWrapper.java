package com.icextreme.findthecheese.wrappers;

import com.icextreme.findthecheese.model.Game;

/**
 * Represents the game wrapper object that is sent
 * to the controller.
 */
public class ApiGameWrapper {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;

    public static ApiGameWrapper makeFromGame(Game game, int id) {
        ApiGameWrapper wrapper = new ApiGameWrapper();
        wrapper.gameNumber = id;

        wrapper.isGameWon = game
                .getBoard()
                .isGameWon();

        wrapper.isGameLost = game
                .getBoard()
                .isGameLost();

        wrapper.numCheeseFound = game
                .getBoard()
                .getMouse()
                .getCheeseEaten();

        wrapper.numCheeseGoal = game
                .getBoard()
                .getMouse()
                .getMaxCheese();

        return wrapper;
    }
}
