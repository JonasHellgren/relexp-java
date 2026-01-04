package chapters.ch12.inv_pendulum.plotting;

import lombok.AllArgsConstructor;
import java.util.function.Function;


@AllArgsConstructor
public enum MeasuresPendulumTrainingEnum {
    EPISODE("Episode", pm -> pm.episode()),
    RETURN("Return", pm -> pm.sumRewards()),
    LOSS("Loss", pm -> pm.loss()),
    Q0CCW("Q0ccw", pm -> pm.q0ccw()),
    Q0N("Q0n", pm -> pm.q0n()),
    Q0CW("Q0cw", pm -> pm.q0cw()),
    TIME("Time", pm -> pm.time()),
    N_STEPS("Nof. steps", pm -> pm.nSteps());

    public final String description;
    public final Function<MeasuresPendulumTraining, Double> mapFunction;

    }
