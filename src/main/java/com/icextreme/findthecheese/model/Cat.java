package com.icextreme.findthecheese.model;

/**
 * Represents the cat object in the game.
 */
public class Cat extends BoardObject {
    private boolean onCheese;
    private Board.Directions previousDirection;

    public Cat(int rowPosition, int colPosition) {
        super(rowPosition, colPosition);
        onCheese = false;
    }

    public boolean isOnCheese() {
        return onCheese;
    }

    public void setOnCheese(boolean onCheese) {
        this.onCheese = onCheese;
    }

    public void setPreviousDirection(Board.Directions direction) {
        this.previousDirection = direction;
    }

    public Board.Directions getPreviousDirection() {
        return previousDirection;
    }
}
