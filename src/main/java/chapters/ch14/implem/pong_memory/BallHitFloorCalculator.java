package chapters.ch14.implem.pong_memory;

import chapters.ch14.domain.environment.EnvironmentI;
import chapters.ch14.implem.pong.ActionPong;
import chapters.ch14.implem.pong.PongSettings;
import chapters.ch14.implem.pong.StatePong;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * This class calculates the time it takes for the ball to hit the floor in the Pong game.
 * It does this by simulating the game until the ball hits the floor.
 * The simulation is performed by calling the step method of the environment object.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BallHitFloorCalculator {

    private EnvironmentI<StatePong, ActionPong> environment;
    PongSettings settings;

    public static BallHitFloorCalculator of(EnvironmentI<StatePong, ActionPong> environment,
                                            PongSettings settings) {
        return new BallHitFloorCalculator(environment, settings);
    }

    public BallHitFloorResult calculate(StatePong state) {
        double dt = settings.timeStep();
        boolean isBottomReached = false;
        var s = state.copy();
        double time = 0;
        while (!isBottomReached) {
            var sr = environment.step(s, ActionPong.still);
            isBottomReached = sr.stateNew().finiteState().isBallBottom();
            s = sr.stateNew();
            time += dt;
            if (time > settings.maxTime()) {
                throw new IllegalStateException("time=" + time + " > maxTime=" + settings.maxTime());
            }
        }
        return BallHitFloorResult.builder()
                .timeHit(time)
                .xBall(s.posBall().x())
                .build();
    }

}
