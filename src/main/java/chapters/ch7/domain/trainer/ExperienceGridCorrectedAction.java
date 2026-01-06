package chapters.ch7.domain.trainer;


import core.gridrl.ExperienceGrid;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import core.gridrl.StepReturnGrid;

/**
 * Represents a corrected experience in a grid environment.
 * An experience consists of a state, an action taken in that state, and the resulting step return.
 * The corrected action is the action that should have been taken in that state.
 */
public record ExperienceGridCorrectedAction(ExperienceGrid experienceGrid, ActionGrid actionCorrected) {

    public static ExperienceGridCorrectedAction of(ExperienceGrid experienceGrid, ActionGrid actionCorrected) {
        return new ExperienceGridCorrectedAction(experienceGrid, actionCorrected);
    }


    public static ExperienceGridCorrectedAction ofSars(StateGrid state,
                                                       ActionGrid action,
                                                       ActionGrid actionCorrected,
                                                       StepReturnGrid sr) {
        return of(ExperienceGrid.ofSars(state, action, sr), actionCorrected);
    }

    public boolean isCorrected() {
        return !actionCorrected.equals(experienceGrid().action());
    }

    public ActionGrid actionAgent() {
        return experienceGrid().action();
    }

    public StateGrid state() {
        return experienceGrid().state();
    }

    public StepReturnGrid stepReturn() {
        return experienceGrid().stepReturn();
    }
}
