package chapters.ch10.bandit.factory;

import chapters.ch10.bandit.domain.environment.EnvironmentParametersBandit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryEnvironmentParametersBandit {

    public static final int REWARD_COIN = 1;
    public static final double HIGH_PROB = 0.9;
    public static final double MODERATE_PROB = 0.6;

    public static EnvironmentParametersBandit veryHighLeftProbability() {
        return EnvironmentParametersBandit.of(HIGH_PROB, REWARD_COIN);
    }

    public static EnvironmentParametersBandit veryHighRightProbability() {
        return EnvironmentParametersBandit.of(1-HIGH_PROB, REWARD_COIN);
    }


    public static EnvironmentParametersBandit highLeftProbability() {
        return EnvironmentParametersBandit.of(MODERATE_PROB, REWARD_COIN);
    }

    public static EnvironmentParametersBandit highRightProbability() {
        return EnvironmentParametersBandit.of(1-MODERATE_PROB, REWARD_COIN);
    }

}
