package chapters.ch12.factory;

import chapters.ch12.domain.inv_pendulum.environment.param.PendulumParameters;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PendulumParametersFactory {

    public static PendulumParameters createForTest() {
        return PendulumParameters.builder()
                .inertia(0.33)
                .length(1.0)
                .m(1)
                .dt(0.01)
                .g(9.81)
                .rewardFail(-100)
                .rewardStep(0)
                .angleMax(Math.PI / 4)
                .angleSpeedMax(3d)
                .lambdaEnLoss(1.0)
                .lambdaFail(1.0)
                .maxTime(5)
                .build();
    }


    public static PendulumParameters createForTrainerTest() {
        return createForTest()
                .withDt(0.1)
                .withMaxTime(10)
                .withAngleMax(Math.PI / 4)
                .withLambdaEnLoss(0.0)
                .withRewardFail(-1)
                .withRewardStep(0);
    }

    public static PendulumParameters createForTrainerRunningFailPenalty() {
        return createForTrainerTest()
                .withLambdaEnLoss(0.1);
    }


    public static PendulumParameters createForTrainerRunningCloserToRefReward() {
        return createForTrainerRunningFailPenalty()
                .withLambdaFail(0);
    }

}
