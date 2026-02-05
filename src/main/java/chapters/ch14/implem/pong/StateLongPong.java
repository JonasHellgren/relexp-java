package chapters.ch14.implem.pong;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * This class  represents the state of the Pong game used by long term memory
 * Includes time until ball hits bottom and deltaX. deltaX is the distance between ball center and paddle
 * when ball hits bottom (assuming present paddle position).
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StateLongPong {

    double timeHit;
    double deltaX;

    public static StateLongPong of(double timeHit, double deltaX) {
        return new StateLongPong(timeHit,deltaX);
    }

    public double timeHit() {
        return timeHit;
    }

    public double deltaX() {
        return deltaX;
    }

    @Override
    public String toString() {
        return "StateLongPong1{" +
                "timeHit=" + timeHit +
                ", deltaX=" + deltaX +
                '}';
    }

}
