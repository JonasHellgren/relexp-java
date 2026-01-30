package chapters.ch12.plotting_invpend;

import lombok.Builder;

@Builder
public record MeasuresPendulumTraining(
        double episode,
        double sumRewards,
        double loss,
        double q0ccw,
        double q0n,
        double q0cw,
        double time,
        double nSteps
) {

}
