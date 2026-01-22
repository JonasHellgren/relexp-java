package chapters.ch11.domain.environment.core;

import chapters.ch11.domain.environment.param.LunarParameters;
import core.foundation.util.unit_converter.UnitConverterUtil;
import core.foundation.util.unit_converter.NonSIUnitsUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import tec.units.ri.unit.Units;

/**
 * Environment class for the Lunar Lander problem.
 * This class implements the EnvironmentI interface and provides methods for
 * stepping the environment and calculating rewards.
 */

@Getter
@AllArgsConstructor
public class EnvironmentLunar implements EnvironmentI {

    LunarParameters parameters;

    public static EnvironmentLunar createDefault() {
        return new EnvironmentLunar(LunarParameters.defaultProps());
    }

    public static EnvironmentI of(LunarParameters ep) {
        return new EnvironmentLunar(ep);
    }

    /**
     * Steps the environment forward in time, given the current state and action.
     * @param state the current state of the environment
     * @param action the action to take in the environment
     * @return the new state and reward after taking the action
     */

    @Override
    public StepReturnLunar step(StateLunar state, double action) {
        double dt = parameters.dt();
        double speed0 = state.variables.spd();
        double y0 = state.variables.y();
        double acc = acceleration(action);
        double speed = speed0 + acc * dt;
        double y = y0 + speed * dt;
        var stateNew = StateLunar.of(y, speed);
        boolean isTerminal = isLanded(y) || isToHighPosition(y);
        boolean isFail = isLanded(y)  && isToHighSpeed(speed) || isToHighPosition(y);
        double reward = calculateReward(y, speed, isFail);
        return StepReturnLunar.builder()
                .stateNew(stateNew)
                .isFail(isFail)
                .isTerminal(isTerminal)
                .reward(reward)
                .build();
    }

    public double acceleration(double action) {
        double m = parameters.massLander();
        double force = parameters.clippedForce(action);
        return (forceInNewton(force) - m * parameters.g()) / m;
    }

    public static double forceInKiloNewton(double forceInNewton) {
        return UnitConverterUtil.convertForce(forceInNewton, Units.NEWTON, NonSIUnitsUtil.KILO_NEWTON);
    }

    public static double forceInNewton(double forceInKiloNewton) {
        return UnitConverterUtil.convertForce(forceInKiloNewton, NonSIUnitsUtil.KILO_NEWTON, Units.NEWTON);
    }

    private boolean isLanded(double y) {
        return y < parameters.ySurface();
    }

    private boolean isToHighPosition(double y) {
        return y > parameters.yMax();
    }

    private double calculateReward(double y, double speed,  boolean isFail) {
        boolean isSuccess = isLanded(y) && !isToHighSpeed(speed);
        double rewardSuccess = isSuccess ? parameters.rewardSuccess() : 0d;
        double rewardFail = isFail ? parameters.rewardFail() : 0d;
        return parameters.rewardStep() + rewardFail + rewardSuccess;
    }

    private boolean isToHighSpeed(double speed) {
        return Math.abs(speed) > Math.abs(parameters.spdLimitCrash());
    }

}
