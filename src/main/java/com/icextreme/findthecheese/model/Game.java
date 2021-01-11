package com.icextreme.findthecheese.model;

/**
 * Represents the game class.
 * <p>
 * This class "begins" the game by creating
 * a board class. Contains a method to initialize
 * the board and its object.
 */
public class Game {
    Board board;

    public Game() {
        board = new Board();
        initialize();
    }

    public void initialize() {
        board.setCells();
        board.setVisitedOuterWall();

        board.recursiveBacktrack(1, 1);
        board.removeWalls();

        //Check for bad maze
        boolean reGenerate = board.checkConstraint();

        // Regenerate maze to a good maze
        while (!reGenerate) {
            board.setCells(); // Reset board to all walls
            board.setVisitedOuterWall();

            board.recursiveBacktrack(1, 1);
            board.removeWalls();

            reGenerate = board.checkConstraint();
        }

        board.spawnMouse();
        board.mouseLookAround();
        board.spawnCats();
        board.spawnCheese();
    }

    public Board getBoard() {
        return board;
    }
}


