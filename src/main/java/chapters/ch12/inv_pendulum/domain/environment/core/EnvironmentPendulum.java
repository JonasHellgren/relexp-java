package chapters.ch12.inv_pendulum.domain.environment.core;

import chapters.ch12.inv_pendulum.domain.environment.param.PendulumParameters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EnvironmentPendulum {

    private PendulumParameters parameters;

    public static EnvironmentPendulum of(PendulumParameters parameters) {
        return new EnvironmentPendulum(parameters);
    }

    public StepReturnPendulum step(StatePendulum state, ActionPendulum action) {
        var stateNew = getStateNew(state, action);
        boolean isFail = Math.abs(stateNew.angle()) > parameters.angleMax();
        boolean isTerminal = isFail || state.nSteps() >= parameters.maxSteps();
        return StepReturnPendulum.builder()
                .stateNew(stateNew)
                .isFail(isFail)
                .isTerminal(isTerminal)
                .reward(getReward(action, stateNew, isFail))
                .build();
    }

    private double getReward(ActionPendulum action, StatePendulum stateNew, boolean isFail) {
        var pp = parameters;
        var dE=Math.abs(action.torque() * stateNew.angularSpeed()) * pp.dt();
        return pp.lambdaEnLoss() * -dE +
                pp.lambdaFail() * (isFail ? pp.rewardFail() : 0d);
    }

    /**
     * The updated velocity (angleSpdNew) to compute the new nOccupied. This is called semi-implicit Euler
     * (a.k.a. symplectic Euler), which is more stable than standard explicit Euler, and in many physics
     * engines this is actually preferred.
     */

    private StatePendulum getStateNew(StatePendulum state, ActionPendulum action) {
        var pp = parameters;
        double angle = state.angle();
        double angleSpd = state.angularSpeed();
        double angleAcc = (action.torque() + pp.length() / 2 * Math.sin(angle) * pp.m() * pp.g()) / pp.inertia();
        double angleSpdNew = angleSpd + angleAcc * pp.dt();
        double angleNew = angle + angleSpdNew * pp.dt();
        return StatePendulum.of(angleNew, angleSpdNew, state.nSteps() + 1);
    }

}
