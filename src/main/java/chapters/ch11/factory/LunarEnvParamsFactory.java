package chapters.ch11.factory;

import chapters.ch11.domain.environment.param.LunarParameters;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LunarEnvParamsFactory {

    public static LunarParameters produceDefault() {
        return LunarParameters.builder()
                .massLander(500.0)
                .dt(0.2)
                .g(1.62)
                .forceMin(0)
                .forceMax(2)
                .ySurface(0d)
                .yMax(5)
                .spdMax(5)
                .spdLimitCrash(1d)
                .rewardFail(-100)
                .rewardSuccess(100)
                .rewardStep(-1)
                .build();
    }

}
