package chapters.ch5.implem.splitting;

import chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath;
import chapters.ch5.domain.environment.StepReturnMc;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Adapter class for the SplittingPath environment.
 * This class provides a bridge between the SplittingPath environment and the Monte Carlo algorithm.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SplittingPathAdapter {

    EnvironmentSplittingPath environment;


    public static SplittingPathAdapter of(EnvironmentSplittingPath environment) {
        return new SplittingPathAdapter(environment);
    }

    public StepReturnMc step(StateSplittingMc stateMc, ActionSplittingMc actionMc) {
        var state= getStateGrid(stateMc);
        var action= getActionGrid(actionMc);
        var stepReturn = environment.step(state, action);
        var stateGrid = stepReturn.sNext();
        return StepReturnMc.builder()
                .sNext(getStateGridMc(stateGrid))
                .isFail(stepReturn.isFail())
                .isTerminal(stepReturn.isTerminal())
                .reward(stepReturn.reward())
                .build();

    }

    public static ActionGrid getActionGrid(ActionSplittingMc actionMc) {
        return actionMc.action;
    }

    public static StateSplittingMc getStateGridMc(StateGrid stateGrid) {
        return StateSplittingMc.of(stateGrid.x(), stateGrid.y());
    }


    public static StateGrid getStateGrid(StateSplittingMc stateMc) {
        return StateGrid.of(stateMc.x(), stateMc.y());
    }

}
