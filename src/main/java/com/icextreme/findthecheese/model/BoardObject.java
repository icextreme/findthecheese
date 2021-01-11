package com.icextreme.findthecheese.model;

/**
 * Represents the board objects on the board.
 * <p>
 * This is a base class that board objects in the game
 * such as cat and mouse can inherit from it.
 */
public class BoardObject {
    protected int rowPosition;
    protected int colPosition;

    protected BoardObject(int rowPosition, int colPosition) {
        this.rowPosition = rowPosition;
        this.colPosition = colPosition;
    }

    public int getRowPosition() {
        return rowPosition;
    }

    public int getColPosition() {
        return colPosition;
    }

    public void moveUp() {
        rowPosition--;
    }

    public void moveDown() {
        rowPosition++;
    }

    public void moveLeft() {
        colPosition--;
    }

    public void moveRight() {
        colPosition++;
    }
}
