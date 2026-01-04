package chapters.ch14.environments.pong;

/**
 * This class represents a 2D velocity vector with double coordinates.
 * It is used in the Pong game to represent the velocity of the ball.
 */
public record VelocityXy(
    double x,
    double y)  {
    public static VelocityXy of(double v, double newPsi) {
        return new VelocityXy(v*Math.cos(newPsi), v*Math.sin(newPsi));
    }

    public static VelocityXy zero() {
        return new VelocityXy(0, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VelocityXy that = (VelocityXy) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
    }


    public double abs() {
        return Math.sqrt(x*x + y*y);
    }
}
