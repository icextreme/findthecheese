package com.icextreme.findthecheese.model;

import java.util.*;

/**
 * Represents the board in the game.
 * <p>
 * The board is used to generate the maze as well as
 * handle the logic of the game.
 */
public class Board {
    // Variable for maze initialization
    private static final int ROWS = 17;
    private static final int COLUMNS = 17;

    // Variables for outer wall generation
    private static final int ROWS_INDEX = ROWS - 1;
    private static final int COLUMNS_INDEX = COLUMNS - 1;

    // Variables for playing field
    private static final int MAX_ROWS = ROWS_INDEX - 1;
    private static final int MAX_COLUMNS = COLUMNS_INDEX - 1;

    private Cell[][] maze;

    public enum Directions { // Need public access for keeping track of backtracking
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private Mouse mouse;
    private List<Cat> cats;
    private Cheese cheese;

    public Board() {
        maze = new Cell[ROWS][COLUMNS];
    }

    public void setCells() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                maze[i][j] = new Cell(i, j);
                maze[i][j].setType(Cell.Type.WALL);
                maze[i][j].setVisited(false);
                maze[i][j].setSpace(Cell.Space.UNEXPLORED);
            }
        }
    }

    public Mouse getMouse() {
        return mouse;
    }

    // Maze generation methods
    // --------------------------

    // Set the outer wall as visited so that
    // the backtrack does not generate a maze there.
    public void setVisitedOuterWall() {
        for (int i = 0; i < COLUMNS; i++) {
            // Top row of outer wall
            maze[0][i].setVisited(true);
            maze[0][i].setSpace(Cell.Space.EXPLORED);

            // Bottom row of outer wall
            maze[ROWS_INDEX][i].setVisited(true);
            maze[ROWS_INDEX][i].setSpace(Cell.Space.EXPLORED);
        }

        for (int i = 0; i < ROWS; i++) {
            // Left most column of wall
            maze[i][0].setVisited(true);
            maze[i][0].setSpace(Cell.Space.EXPLORED);

            // Right most column of wall
            maze[i][COLUMNS_INDEX].setVisited(true);
            maze[i][COLUMNS_INDEX].setSpace(Cell.Space.EXPLORED);
        }
    }

    public void recursiveBacktrack(int row, int column) {
        Directions[] directions = Directions.values();
        Collections.shuffle(Arrays.asList(directions));

        Cell current = maze[row][column];
        setCellVisited(current);

        for (Directions d : directions) {
            int rowPosition = row + nextRowDirection(d);
            int colPosition = column + nextColumnDirection(d);

            boolean isInRowBounds = (rowPosition >= 0
                    && rowPosition < ROWS_INDEX);

            boolean isInColumnBounds = (colPosition >= 0
                    && colPosition < COLUMNS_INDEX);

            if (isInRowBounds && isInColumnBounds) {
                Cell neighbourCell = maze[rowPosition][colPosition];

                if (!neighbourCell.isVisited()) {
                    Cell inBetweenCell = inBetweenCell(current, neighbourCell);
                    setCellVisited(inBetweenCell);

                    recursiveBacktrack(neighbourCell.getRowPosition(), neighbourCell.getColPosition());
                }
            }
        }
    }

    private void setCellVisited(Cell c) {
        c.setType(Cell.Type.EMPTY);
        c.setSpace(Cell.Space.UNEXPLORED);
        c.setVisited(true);
    }

    public void removeWalls() {
        Random r = new Random();

        int i = 3;

        // Add loops to maze to avoid cornered by cat
        while (i > 0) {
            int randomColumn = r.nextInt(MAX_COLUMNS - 1) + 1;
            int randomRow = r.nextInt(MAX_ROWS) + 1;

            boolean okayToRemove = maze[randomRow][randomColumn].getType() == Cell.Type.WALL
                    && maze[randomRow][randomColumn + 1].getType() == Cell.Type.WALL
                    && maze[randomRow][randomColumn - 1].getType() == Cell.Type.WALL
                    && maze[randomRow + 1][randomColumn].getType() == Cell.Type.EMPTY
                    && maze[randomRow - 1][randomColumn].getType() == Cell.Type.EMPTY;

            if (okayToRemove) {
                maze[randomRow][randomColumn].setType(Cell.Type.EMPTY);
                maze[randomRow][randomColumn].setSpace(Cell.Space.UNEXPLORED);
                i--;
            }
        }

        maze[1][1].setType(Cell.Type.EMPTY);
        maze[1][1].setSpace(Cell.Space.UNEXPLORED);

        maze[1][MAX_COLUMNS].setType(Cell.Type.EMPTY);
        maze[1][MAX_COLUMNS].setSpace(Cell.Space.UNEXPLORED);

        maze[MAX_ROWS][1].setType(Cell.Type.EMPTY);
        maze[MAX_ROWS][1].setSpace(Cell.Space.UNEXPLORED);

        maze[MAX_ROWS][MAX_COLUMNS].setType(Cell.Type.EMPTY);
        maze[MAX_ROWS][MAX_COLUMNS].setSpace(Cell.Space.UNEXPLORED);
    }

    private Cell inBetweenCell(Cell cell, Cell neighbourCell) {
        boolean sameRow = cell.getRowPosition() == neighbourCell.getRowPosition();
        boolean sameColumn = cell.getColPosition() == neighbourCell.getColPosition();

        assert (!(sameRow && sameColumn));

        if (sameRow) {
            if (cell.getColPosition() < neighbourCell.getColPosition()) {
                return maze[neighbourCell.getRowPosition()][neighbourCell.getColPosition() - 1];
            } else {
                return maze[neighbourCell.getRowPosition()][neighbourCell.getColPosition() + 1];
            }
        }

        if (sameColumn) {
            if (cell.getRowPosition() < neighbourCell.getRowPosition()) {
                return maze[neighbourCell.getRowPosition() - 1][neighbourCell.getColPosition()];
            } else {
                return maze[neighbourCell.getRowPosition() + 1][neighbourCell.getColPosition()];
            }
        }

        // This should never happen
        throw new RuntimeException();
    }

    private int nextRowDirection(Directions d) {
        if (d == Directions.UP) {
            return -2;
        } else if (d == Directions.DOWN) {
            return 2;
        } else {
            return 0;
        }
    }

    private int nextColumnDirection(Directions d) {
        if (d == Directions.LEFT) {
            return -2;
        } else if (d == Directions.RIGHT) {
            return 2;
        } else {
            return 0;
        }
    }

    // Check for 2x2 empty box space
    private boolean checkBox(int row, int column) {
        boolean Left = maze[row][column - 1].getType() == Cell.Type.EMPTY;
        boolean Down = maze[row + 1][column].getType() == Cell.Type.EMPTY;
        boolean bottomLeft = maze[row + 1][column - 1].getType() == Cell.Type.EMPTY;
        boolean current = maze[row][column].getType() == Cell.Type.EMPTY;

        return Left && Down && bottomLeft && current;
    }

    public boolean checkConstraint() {
        for (int i = 1; i < ROWS_INDEX; i++) {
            for (int j = 1; j < COLUMNS_INDEX; j++) {
                if (checkBox(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    // Gameplay methods
    // --------------------------
    public void spawnCats() {
        maze[1][MAX_COLUMNS].setType(Cell.Type.CAT);
        maze[1][MAX_COLUMNS].setSpace(Cell.Space.UNEXPLORED);

        maze[MAX_ROWS][1].setType(Cell.Type.CAT);
        maze[MAX_ROWS][1].setSpace(Cell.Space.UNEXPLORED);

        maze[MAX_ROWS][MAX_COLUMNS].setType(Cell.Type.CAT);
        maze[MAX_ROWS][MAX_COLUMNS].setSpace(Cell.Space.UNEXPLORED);

        cats = new ArrayList<>();
        cats.add(new Cat(1, MAX_COLUMNS));
        cats.add(new Cat(MAX_ROWS, 1));
        cats.add(new Cat(MAX_ROWS, MAX_COLUMNS));
    }

    public void spawnCheese() {
        Random r = new Random();
        boolean setCheese = false;

        while (!setCheese) { //Ensure cheese is not on a wall
            Cell c = maze[r.nextInt(MAX_ROWS) + 1][r.nextInt(MAX_COLUMNS) + 1];
            if ((c.getType() != Cell.Type.WALL) && (c.getType() != Cell.Type.MOUSE)) {
                c.setType(Cell.Type.CHEESE);
                cheese = new Cheese(c.rowPosition, c.colPosition);
                setCheese = true;
            }
        }
    }

    public void spawnMouse() {
        maze[1][1].setType(Cell.Type.MOUSE);
        maze[1][1].setSpace(Cell.Space.EXPLORED);
        mouse = new Mouse(1, 1);
    }

    public void mapCheat() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (maze[i][j].getSpace() == Cell.Space.UNEXPLORED) {
                    maze[i][j].setSpace(Cell.Space.EXPLORED);
                }
            }
        }
    }

    public void cheeseCheat() {
        mouse.setMaxCheese(1);
    }

    public void moveCats() {
        Directions[] directions = Directions.values();

        for (Cat cat : cats) {
            boolean moved = false;

            Collections.shuffle(Arrays.asList(directions));

            for (Directions d : directions) {
                if (!catMoveIntoWall(d, cat) && (cat.getPreviousDirection() != d)) {
                    if (cat.isOnCheese()) {
                        maze[cat.getRowPosition()][cat.getColPosition()].setType(Cell.Type.CHEESE);
                    } else {
                        maze[cat.getRowPosition()][cat.getColPosition()].setType(Cell.Type.EMPTY);
                    }

                    if (d == Directions.UP) {
                        cat.moveUp();
                    } else if (d == Directions.DOWN) {
                        cat.moveDown();
                    } else if (d == Directions.LEFT) {
                        cat.moveLeft();
                    } else if (d == Directions.RIGHT) {
                        cat.moveRight();
                    }

                    boolean moveIntoCheeseOrEmptyOrCat = maze[cat.getRowPosition()][cat.getColPosition()].getType() == Cell.Type.CHEESE
                            || maze[cat.getRowPosition()][cat.getColPosition()].getType() == Cell.Type.EMPTY
                            || maze[cat.getRowPosition()][cat.getColPosition()].getType() == Cell.Type.CAT;

                    if (moveIntoCheeseOrEmptyOrCat) {
                        if (maze[cat.getRowPosition()][cat.getColPosition()].getType() == Cell.Type.CHEESE) {
                            cat.setOnCheese(true);
                        } else {
                            cat.setOnCheese(false);
                        }
                        maze[cat.getRowPosition()][cat.getColPosition()].setType(Cell.Type.CAT);
                    } else if (maze[cat.getRowPosition()][cat.getColPosition()].getType() == Cell.Type.MOUSE) {
                        mouse.setEaten(true);
                        maze[mouse.getRowPosition()][mouse.getColPosition()].setType(Cell.Type.DEAD_MOUSE);
                    } else {
                        throw new RuntimeException("Went into cell either NOT a Mouse or Cheese or Empty Or Cat");
                    }

                    moved = true;

                    // Set opposite position to prevent backtracking
                    if (d == Directions.UP) {
                        cat.setPreviousDirection(Directions.DOWN);
                    } else if (d == Directions.DOWN) {
                        cat.setPreviousDirection(Directions.UP);
                    } else if (d == Directions.LEFT) {
                        cat.setPreviousDirection(Directions.RIGHT);
                    } else if (d == Directions.RIGHT) {
                        cat.setPreviousDirection(Directions.LEFT);
                    }

                    break; // Break out of loop to prevent further moving
                }
            } // for directions

            // This part gets activated when the cat has no more moves
            // (it is cornered)
            if (!moved) {
                maze[cat.getRowPosition()][cat.getColPosition()].setType(Cell.Type.EMPTY);

                if (cat.getPreviousDirection() == Directions.UP) {
                    cat.moveUp();
                    cat.setPreviousDirection(Directions.DOWN);
                } else if (cat.getPreviousDirection() == Directions.DOWN) {
                    cat.moveDown();
                    cat.setPreviousDirection(Directions.UP);
                } else if (cat.getPreviousDirection() == Directions.LEFT) {
                    cat.moveLeft();
                    cat.setPreviousDirection(Directions.RIGHT);
                } else if (cat.getPreviousDirection() == Directions.RIGHT) {
                    cat.moveRight();
                    cat.setPreviousDirection(Directions.LEFT);
                }

                boolean moveIntoCheeseOrEmptyOrCat = maze[cat.getRowPosition()][cat.getColPosition()].getType() == Cell.Type.CHEESE
                        || maze[cat.getRowPosition()][cat.getColPosition()].getType() == Cell.Type.EMPTY
                        || maze[cat.getRowPosition()][cat.getColPosition()].getType() == Cell.Type.CAT;

                if (moveIntoCheeseOrEmptyOrCat) {
                    if (maze[cat.getRowPosition()][cat.getColPosition()].getType() == Cell.Type.CHEESE) {
                        cat.setOnCheese(true);
                    }
                    maze[cat.getRowPosition()][cat.getColPosition()].setType(Cell.Type.CAT);
                } else if (maze[cat.getRowPosition()][cat.getColPosition()].getType() == Cell.Type.MOUSE) {
                    mouse.setEaten(true);
                    maze[mouse.getRowPosition()][mouse.getColPosition()].setType(Cell.Type.DEAD_MOUSE);
                } else {
                    throw new RuntimeException("Went into cell either NOT a Mouse or Cheese or Empty Or Cat");
                }
            }
        } // for cats
    }

    public void moveMouse(String s) {
        maze[mouse.getRowPosition()][mouse.getColPosition()]
                .setType(Cell.Type.EMPTY);
        maze[mouse.getRowPosition()][mouse.getColPosition()]
                .setSpace(Cell.Space.EXPLORED);

        switch (s) {
            case "w":
                mouse.moveUp();
                break;
            case "a":
                mouse.moveLeft();
                break;
            case "s":
                mouse.moveDown();
                break;
            case "d":
                mouse.moveRight();
                break;
            default:
                assert false;
        }

        boolean moveIntoCheeseOrEmpty = maze[mouse.getRowPosition()][mouse.getColPosition()].getType() == Cell.Type.CHEESE
                || maze[mouse.getRowPosition()][mouse.getColPosition()].getType() == Cell.Type.EMPTY;

        if (moveIntoCheeseOrEmpty) {
            if (maze[mouse.getRowPosition()][mouse.getColPosition()].getType() == Cell.Type.CHEESE) {
                mouse.eatCheese();
                if (!(mouse.getCheeseEaten() == mouse.getMaxCheese())) {
                    spawnCheese();
                }
            }
            maze[mouse.getRowPosition()][mouse.getColPosition()].setType(Cell.Type.MOUSE);
            mouseLookAround();
        } else if (maze[mouse.getRowPosition()][mouse.getColPosition()].getType() == Cell.Type.CAT) {
            mouse.setEaten(true);
            maze[mouse.getRowPosition()][mouse.getColPosition()].setType(Cell.Type.DEAD_MOUSE);
        } else {
            throw new RuntimeException("Went into cell either NOT a Cat or Cheese or Empty");
        }
    }

    public boolean mouseMoveIntoWall(String s) {
        switch (s) {
            case "w":
                return maze[mouse.getRowPosition() - 1][mouse.getColPosition()]
                        .getType() == Cell.Type.WALL;
            case "a":
                return maze[mouse.getRowPosition()][mouse.getColPosition() - 1]
                        .getType() == Cell.Type.WALL;
            case "s":
                return maze[mouse.getRowPosition() + 1][mouse.getColPosition()]
                        .getType() == Cell.Type.WALL;
            case "d":
                return maze[mouse.getRowPosition()][mouse.getColPosition() + 1]
                        .getType() == Cell.Type.WALL;
            default:
                assert false;
        }
        return false;
    }

    public boolean catMoveIntoWall(Directions dir, Cat cat) {// Return true if a cat would move into a wall
        switch (dir) {
            case UP:
                return maze[cat.getRowPosition() - 1][cat.getColPosition()]
                        .getType() == Cell.Type.WALL;
            case LEFT:
                return maze[cat.getRowPosition()][cat.getColPosition() - 1]
                        .getType() == Cell.Type.WALL;
            case DOWN:
                return maze[cat.getRowPosition() + 1][cat.getColPosition()]
                        .getType() == Cell.Type.WALL;
            case RIGHT:
                return maze[cat.getRowPosition()][cat.getColPosition() + 1]
                        .getType() == Cell.Type.WALL;
            default:
                assert false;
        }
        return false;
    }

    public void mouseLookAround() {
        maze[mouse.getRowPosition() + 1][mouse.getColPosition()]
                .setSpace(Cell.Space.EXPLORED);

        maze[mouse.getRowPosition() - 1][mouse.getColPosition()]
                .setSpace(Cell.Space.EXPLORED);

        maze[mouse.getRowPosition()][mouse.getColPosition() + 1]
                .setSpace(Cell.Space.EXPLORED);

        maze[mouse.getRowPosition()][mouse.getColPosition() - 1]
                .setSpace(Cell.Space.EXPLORED);

        maze[mouse.getRowPosition() + 1][mouse.getColPosition() + 1]
                .setSpace(Cell.Space.EXPLORED);

        maze[mouse.getRowPosition() + 1][mouse.getColPosition() - 1]
                .setSpace(Cell.Space.EXPLORED);

        maze[mouse.getRowPosition() - 1][mouse.getColPosition() + 1]
                .setSpace(Cell.Space.EXPLORED);

        maze[mouse.getRowPosition() - 1][mouse.getColPosition() - 1]
                .setSpace(Cell.Space.EXPLORED);
    }

    // Wrapper methods
    // --------------------------

    public int getROWS() {
        return ROWS;
    }

    public int getCOLUMNS() {
        return COLUMNS;
    }

    public boolean[][] isVisible() {
        boolean[][] isVisible = new boolean[ROWS][COLUMNS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (maze[i][j].getSpace() == Cell.Space.EXPLORED) {
                    isVisible[i][j] = true;
                }
            }
        }
        return isVisible;
    }

    public boolean[][] hasWalls() {
        boolean[][] hasWalls = new boolean[ROWS][COLUMNS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (maze[i][j].getType() == Cell.Type.WALL) {
                    hasWalls[i][j] = true;
                }
            }
        }
        return hasWalls;
    }

    public List<Cat> getCats() {
        return cats;
    }

    public boolean isGameWon() {
        return mouse.getCheeseEaten() == mouse.getMaxCheese();
    }

    public boolean isGameLost() {
        return mouse.isEaten();
    }

    public List<Cell> getCatCellLocations() {
        List<Cell> catCellLocations = new ArrayList<>();

        for (Cat cat : getCats()) {
            catCellLocations.add(new Cell(cat.getRowPosition(), cat.getColPosition()));
        }

        return catCellLocations;
    }

    public Cell getMouseCellLocation() {
        return new Cell(mouse.getRowPosition(), mouse.getColPosition());
    }

    public Cell getCheeseCellLocation() {
        return new Cell(cheese.getRowPosition(), cheese.getColPosition());
    }
}