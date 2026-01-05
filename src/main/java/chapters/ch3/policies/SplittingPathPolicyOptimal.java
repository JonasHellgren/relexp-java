package chapters.ch3.policies;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.implem.splitting_path_problem.Informer;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SplittingPathPolicyOptimal implements SplittingPathPolicyI {

    EnvironmentParametersSplitting parameters;

    public static SplittingPathPolicyOptimal of(EnvironmentParametersSplitting parameters) {
        return new SplittingPathPolicyOptimal(parameters);
    }

    @Override
    public ActionGrid chooseAction(StateGrid s) {
        var informer= Informer.create(parameters);
        boolean isAtSplit=informer.isAtSplit(s);
        var actionWhenAtSplitNode= ActionGrid.N;
        return (isAtSplit)
                ? actionWhenAtSplitNode
                : ActionGrid.E;
    }
}
