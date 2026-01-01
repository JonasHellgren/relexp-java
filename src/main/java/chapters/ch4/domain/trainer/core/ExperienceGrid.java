package chapters.ch4.domain.trainer.core;

import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import core.gridrl.StepReturnGrid;

import java.util.Optional;

/**
 * Represents a single experience in a grid environment.
 * An experience consists of a state, an action taken in that state, and the resulting
 * step return.
 */
public record ExperienceGrid(
        StateGrid state,
        ActionGrid action,
        StepReturnGrid stepReturn,
        Optional<ActionGrid> actionNext
) {


    public static ExperienceGrid ofSars(StateGrid state,
                                        ActionGrid action,
                                        StepReturnGrid sr) {
        return new ExperienceGrid(state, action, sr,Optional.empty());
    }

    public static ExperienceGrid ofSarsa(StateGrid state,
                                        ActionGrid action,
                                        StepReturnGrid sr,
                                        ActionGrid actionNext) {
        return new ExperienceGrid(state, action, sr,Optional.of(actionNext));
    }

    public boolean isActionNextPresent() {
        return actionNext.isPresent();
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

    public StateGrid stateNext() {
        return stepReturn.sNext();
    }

    @Override
    public String toString() {
        return "ExperienceGrid{" +
                getStateAndActionAsString() +
                ", stepReturn=" + stepReturn +
                '}';
    }

    public String toStringShort() {
        return "ExperienceGrid{"+ getStateAndActionAsString()+'}';
    }

    private String getStateAndActionAsString() {
        return "state=" + state +", action=" + action.toString();
    }


}
