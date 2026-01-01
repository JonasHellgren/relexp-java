package core.gridrl;

import core.foundation.util.rand.RandUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
/**
 * Enum representing the possible actions in a grid environment.
 * Each action is associated with a GridActionProperties object, which defines the
 * delta x and y values and an arrow symbol.
 */
@AllArgsConstructor
@Getter
public enum ActionGrid  {
    N(GridActionProperties.of(0,1,"↑")),
    E(GridActionProperties.of(1,0,"→")),
    S(GridActionProperties.of(0,-1,"↓")),
    W(GridActionProperties.of(-1,0,"←"));

    private final GridActionProperties properties;

    public static List<ActionGrid> getAllActions() {
        return List.of(ActionGrid.N, ActionGrid.E, ActionGrid.S, ActionGrid.W);
    }

    public static int maxNofActions() {
        return getAllActions().size();
    }

    public static ActionGrid random() {
        return getAllActions().get(RandUtils.getRandomIntNumber(0, maxNofActions()));
    }

    public int deltaX() {return properties.deltaX();}

    public int deltaY() {return properties.deltaY();}

    @Override
    public String toString() {
        return properties.arrow();
    }

}
