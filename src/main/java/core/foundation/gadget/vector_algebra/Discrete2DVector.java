package core.foundation.gadget.vector_algebra;

/**
 * Represents a 2D vector with discrete coordinates.
 *
 * @param dx the change in the x-coordinate
 * @param dy the change in the y-coordinate
 */

public record Discrete2DVector(
        int dx,
        int dy
) {

    public static Discrete2DVector of(int dx, int dy) {
        return new Discrete2DVector(dx,dy);
    }

    public boolean equals(Discrete2DVector other) {
        return dx==other.dx && dy==other.dy;
    }


}
