package com.icextreme.findthecheese.wrappers;

import com.icextreme.findthecheese.model.*;

import java.util.List;

/**
 * Represents the board wrapper object that is sent
 * to the controller.
 */
public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper mouseLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;

    public static ApiBoardWrapper makeFromGame(Game game) {
        ApiBoardWrapper wrapper = new ApiBoardWrapper();

        wrapper.boardWidth = game
                .getBoard()
                .getROWS();

        wrapper.boardHeight = game
                .getBoard()
                .getCOLUMNS();

        wrapper.mouseLocation = ApiLocationWrapper
                .makeFromCellLocation(game
                        .getBoard()
                        .getMouseCellLocation());

        wrapper.cheeseLocation = ApiLocationWrapper
                .makeFromCellLocation(game
                        .getBoard()
                        .getCheeseCellLocation()
                );

        wrapper.catLocations = ApiLocationWrapper
                .makeFromCellLocations(game
                        .getBoard()
                        .getCatCellLocations()
                );

        wrapper.hasWalls = game
                .getBoard()
                .hasWalls();

        wrapper.isVisible = game
                .getBoard()
                .isVisible();

        return wrapper;
    }
}
