package com.icextreme.findthecheese.model;

/**
 * Represents the mouse object in the game.
 * <p>
 * Contains a boolean value to detect if it is eaten
 * therefore ending the game and allowing the UI
 * to display an "X".
 */
public class Mouse extends BoardObject {
    private int cheeseEaten = 0;
    private int maxCheese = 5;
    private boolean isEaten = false;

    public Mouse(int rowPosition, int colPosition) {
        super(rowPosition, colPosition);
    }

    public void eatCheese() {
        cheeseEaten++;
    }

    public int getCheeseEaten() {
        return cheeseEaten;
    }

    public int getMaxCheese() {
        return maxCheese;
    }

    public void setMaxCheese(int maxCheese) {
        this.maxCheese = maxCheese;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }
}
