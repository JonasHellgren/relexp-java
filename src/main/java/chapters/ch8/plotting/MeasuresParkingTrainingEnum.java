package chapters.ch8.plotting;

import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
public enum MeasuresParkingTrainingEnum {
    EPISODE("Episode", pm -> pm.step()),
    RETURN("Return", pm -> pm.sumRewards()),
    REWARD_AVG("Average reward", pm -> pm.rewardAverage()),
    NOCCUP_AVG("Average n. occup", pm -> pm.nOoccupAvg());

    public final String description;
    public final Function<MeasuresParkingTraining, Double> mapFunction;
}
