package com.icextreme.findthecheese.model;

/**
 * Represents the cell object.
 * <p>
 * Contains two enums, one for the value of the cell,
 * another for the visibility of space in the cell
 * (whether the space is unexplored or explored
 * by the mouse)
 */
public class Cell extends BoardObject {
    private Type type;
    private Space space;
    private boolean visited; // for maze generation algorithm

    public Cell(int rowPosition, int ColumnPosition) {
        super(rowPosition, ColumnPosition);
        type = Type.EMPTY;
        visited = false;
        space = Space.UNEXPLORED;
    }

    public enum Type {
        EMPTY,
        WALL,
        MOUSE,
        CAT,
        CHEESE,
        DEAD_MOUSE
    }

    public enum Space {
        UNEXPLORED,
        EXPLORED
    }

    public void setType(Type t) {
        this.type = t;
    }

    public Type getType() {
        return type;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public boolean isVisited() {
        return visited;
    }
}
