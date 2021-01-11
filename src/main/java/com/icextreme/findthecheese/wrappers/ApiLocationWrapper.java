package com.icextreme.findthecheese.wrappers;

import com.icextreme.findthecheese.model.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the location wrapper object that is sent
 * to the controller.
 */
public class ApiLocationWrapper {
    public int x;
    public int y;

    public static ApiLocationWrapper makeFromCellLocation(Cell cell) {
        ApiLocationWrapper location = new ApiLocationWrapper();

        location.x = cell.getColPosition();
        location.y = cell.getRowPosition();

        return location;
    }

    public static List<ApiLocationWrapper> makeFromCellLocations(Iterable<Cell> cells) {
        List<ApiLocationWrapper> locations = new ArrayList<>();

        for (Cell cell : cells) {
            locations.add(makeFromCellLocation(cell));
        }

        return locations;
    }
}
