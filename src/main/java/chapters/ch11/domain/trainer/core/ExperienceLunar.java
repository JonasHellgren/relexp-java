package chapters.ch11.domain.trainer.core;

import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.core.StepReturnLunar;
import core.foundation.util.formatting.NumberFormatterUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a single experience in the lunar lander environment.
 * An experience consists of a state, an action taken in that state,
 * and the resulting step return.
 */
public record ExperienceLunar(
        StateLunar state,
        double action,
        StepReturnLunar stepReturn

) {

    public static final int NOF_DIGITS = 2;

    public static ExperienceLunar of(StateLunar state,
                                     double action,
                                     StepReturnLunar sr) {
        return new ExperienceLunar(state, action, sr);
    }

    public double reward() {
        return stepReturn.reward();
    }

    public boolean isTransitionToTerminal() {
        return stepReturn.isTerminal();
    }

    public boolean isTransitionToFail() {
        return stepReturn.isFail();
    }

    public StateLunar stateNew() {
        return stepReturn.stateNew();
    }

    @Override
    public String toString() {
        return "ExperienceLunar{" +
                getStateAndActionAsString() +
                ", stepReturn=" + stepReturn +
                '}';
    }

    public String toStringShort() {
        return "ExperienceLunar{"+ getStateAndActionAsString()+'}';
    }


    @NotNull
    private String getStateAndActionAsString() {
        return "state=" + state +
                ", action=" + NumberFormatterUtil.getRoundedNumberAsString(action, NOF_DIGITS);
    }


}
