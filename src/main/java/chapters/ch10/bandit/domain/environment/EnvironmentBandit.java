package chapters.ch10.bandit.domain.environment;

import core.foundation.util.rand.RandUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentBandit {

    EnvironmentParametersBandit parameters;

    public static EnvironmentBandit of(EnvironmentParametersBandit parameters) {
        return new EnvironmentBandit(parameters);
    }

    public StepReturnBandit step(ActionBandit action) {
        boolean isLeftArm= action.equals(ActionBandit.LEFT);
        boolean isCoin = (isLeftArm)
                ?  RandUtils.randomNumberBetweenZeroAndOne() < parameters.probCoinLeftArm()
                :  RandUtils.randomNumberBetweenZeroAndOne() < parameters.probCoinRightArm();
        double reward = isCoin ? parameters.rewardCoin() : 0.0;
        return StepReturnBandit.of(reward, isCoin);
    }
}
