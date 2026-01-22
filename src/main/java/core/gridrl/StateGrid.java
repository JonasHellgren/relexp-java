package core.gridrl;

import core.foundation.gadget.pos.PosXyInt;
import core.foundation.util.math.MathUtil;
import lombok.AllArgsConstructor;
import java.util.Objects;

/**
 * Represents a state in a grid environment, defined by its x and y coordinates.
 */
@AllArgsConstructor
public class StateGrid {

    private final VariablesGrid variables;

    public static StateGrid of(int x, int y) {
        return new StateGrid(new VariablesGrid(x, y));
    }

    /**
     * Returns the x coordinate of this state.
     *
     * @return the x coordinate
     */
    public int x() {
        return variables.x();
    }

    /**
     * Returns the y coordinate of this state.
     *
     * @return the y coordinate
     */
    public int y() {
        return variables.y();
    }

    /**
     * Creates a copy of this state.
     *
     * @return a new StateGrid instance with the same coordinates as this one
     */
    public StateGrid copy() {
        return new StateGrid(variables);
    }

    /**
     * Applies the given action to this state and returns the resulting state.
     *
     * @param action the action to apply
     * @return the resulting state
     */
    public StateGrid ofApplyingAction(ActionGrid action) {
        return of(variables.x() + action.deltaX(), variables.y() + action.deltaY());
    }

    /**
     * Clips this state to the given grid parameters, ensuring that the coordinates are within the valid range.
     *
     * @param parameters the grid parameters to clip to
     * @return the clipped state
     *
     * TODO REMOVE METHOD
     */
    public StateGrid clip(EnvironmentGridParametersI parameters) {
        Integer minX = parameters.getPosXMinMax().getFirst();
        Integer maxX = parameters.getPosXMinMax().getSecond();
        Integer minY = parameters.getPosYMinMax().getFirst();
        Integer maxY = parameters.getPosYMinMax().getSecond();
        return StateGrid.of(
                MathUtil.clip(x(), minX, maxX),
                MathUtil.clip(y(), minY, maxY));
    }

/**
 * Clips this state to the given grid parameters, ensuring that the coordinates are within the valid range.
 */
 public StateGrid clip(PosXyInt minXy, PosXyInt maxXy) {
        return StateGrid.of(
                MathUtil.clip(x(), minXy.x(), maxXy.x()),
                MathUtil.clip(y(), minXy.y(), maxXy.y()));
    }



        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return variables.equals(((StateGrid) o).variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x(), y());
    }

    @Override
    public String toString() {
        return "(" + "x=" + variables.x() + ", y=" + variables.y() + ')';
    }

}
