package chapters.ch5.implem.splitting;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath;
import chapters.ch5.domain.environment.ActionMcI;
import chapters.ch5.domain.environment.EnvironmentMcI;
import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.domain.environment.StepReturnMc;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Implementation of the EnvironmentMcI interface for the splitting environment.
 * Reuse via adapter of the environment in folder a_concepts
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentSplittingMc implements EnvironmentMcI {

    public static String NAME="Splitting";

    SplittingPathAdapter splittingPathAdapter;

    public static EnvironmentSplittingMc of(@NonNull EnvironmentParametersSplitting pars) {
        return new EnvironmentSplittingMc(SplittingPathAdapter.of(EnvironmentSplittingPath.of(pars)));
    }


    @Override
    public String name() {
        return "splitting";
    }

    @Override
    public StepReturnMc step(StateMcI state, ActionMcI action) {
        Preconditions.checkArgument(state instanceof StateSplittingMc, "invalid class of state=" + state);
        Preconditions.checkArgument(action instanceof ActionSplittingMc, "invalid class of action=" + action);
        var stateGridMc = (StateSplittingMc) state;
        var actionSplittingMc = (ActionSplittingMc) action;
        return splittingPathAdapter.step(stateGridMc, actionSplittingMc);
    }

}
