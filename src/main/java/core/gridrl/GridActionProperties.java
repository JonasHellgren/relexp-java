package core.gridrl;

/**
 * Represents the properties of an action in a grid environment, supports ActionGrid enum
 * An action consists of a delta in the x and y directions, and an arrow symbol.
 */
public record GridActionProperties(
        int deltaX,
        int deltaY,
        String arrow
) {

    public static GridActionProperties of(int deltaX, int deltaY, String arrow) {
        return new GridActionProperties(deltaX,deltaY,arrow);
    }

}
