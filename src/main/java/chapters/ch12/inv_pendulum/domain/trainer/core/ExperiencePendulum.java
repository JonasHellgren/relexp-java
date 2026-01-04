package chapters.ch12.inv_pendulum.domain.trainer.core;


import chapters.ch12.inv_pendulum.domain.environment.core.ActionPendulum;
import chapters.ch12.inv_pendulum.domain.environment.core.StatePendulum;
import chapters.ch12.inv_pendulum.domain.environment.core.StepReturnPendulum;

/**
 * Represents a single experience in the inverted pendulum environment.
 * An experience consists of a state, an action taken in that state, and the resulting step return.
 */
public record ExperiencePendulum(
        StatePendulum state,
        ActionPendulum action,
        StepReturnPendulum stepReturn
) {

    public static ExperiencePendulum of(StatePendulum state,
                                        ActionPendulum action,
                                        StepReturnPendulum stepReturn) {
        return new ExperiencePendulum(state, action, stepReturn);
    }

    public double reward() {
        return stepReturn.reward();
    }

    public boolean isTerminal() {
        return stepReturn.isTerminal();
    }

    public StatePendulum stateNew() {
        return stepReturn.stateNew();
    }

    @Override
    public String toString() {
        return "ExperiencePendulum{" +
                "state=" + state +
                ", action=" + action +
                ", r=" + stepReturn.reward() +
                ", nSteps="+stepReturn.stateNew().nSteps()+
                '}';
    }

}
