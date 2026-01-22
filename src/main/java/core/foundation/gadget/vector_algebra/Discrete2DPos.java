package core.foundation.gadget.vector_algebra;

import com.google.common.math.IntMath;
import core.foundation.util.math.MathUtil;

import java.util.Optional;


/**
 * Represents a 2D position with integer coordinates.
 */

public record Discrete2DPos(
        int x,
        int y
) {

    public static Discrete2DPos of(int x,int y) {
        return new Discrete2DPos(x,y);
    }

    public Discrete2DPos copy() {
        return  Discrete2DPos.of(x,y);
    }

    /**
     * Moves this position by the given vector.
     *
     * @param vector the vector to move by
     * @return a new Discrete2DPos instance with the updated coordinates
     */

    public Discrete2DPos move(Discrete2DVector vector) {
        return  Discrete2DPos.of(
                x+ vector.dx(),
                y+ vector.dy());
    }


    /**
     * Clips this position to the given bounds.
     *
     * @param minPos the minimum allowed position
     * @param maxPos the maximum allowed position
     * @return a new Discrete2DPos instance with the clipped coordinates
     */

    public Discrete2DPos clip(Discrete2DPos minPos, Discrete2DPos maxPos) {
        return  Discrete2DPos.of(
                MathUtil.clip(x,minPos.x,maxPos.x),
                MathUtil.clip(y,minPos.y,maxPos.y));
    }


    /**
     * Returns the midpoint between this position and the given position, if it exists.
     *
     * @param other the other position
     * @return an Optional containing the midpoint, or an empty Optional if the midpoint does not exist
     */
    public Optional<Discrete2DPos> midPos(Discrete2DPos other) {
        int sumX = x + other.x;
        int sumY = y + other.y;
        return (IntMath.mod(sumX,2)!=0 || IntMath.mod(sumY,2)!=0 )
                ? Optional.empty()
                : Optional.of(Discrete2DPos.of(sumX/2,sumY/2));

    }

    /**
     * Returns the distance between this position and the given position.
     *
     * @param other the other position
     * @return the distance between the two positions
     */

    public double distance(Discrete2DPos other) {
        return distance(this,other);
    }


    /**
     * Returns the vector from this position to the given position.
     *
     * @param other the other position
     * @return the vector from this position to the other position
     */
    public Discrete2DVector vector(Discrete2DPos other) {
        return Discrete2DVector.of(getDx(this, other),getDy(this, other));
    }

    /**
     * Returns the euclidean distance as single number between two positions.
     *
     * @param posA the first position
     * @param posB the second position
     * @return the distance between the two positions
     */

    public static double distance(Discrete2DPos posA, Discrete2DPos posB) {
        int dx= getDx(posA, posB);
        int dy= getDy(posA, posB);
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    private static int getDy(Discrete2DPos posA, Discrete2DPos posB) {
        return posB.y - posA.y;
    }

    private static int getDx(Discrete2DPos posA, Discrete2DPos posB) {
        return posB.x - posA.x;
    }


}
