package chapters.ch2.implem.splitting_path_problem;

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
        boolean isAtSplit=parameters.isAtSplit(s);
        var actionWhenAtSplitNode= ActionGrid.N;
        return (isAtSplit)
                ? actionWhenAtSplitNode
                : ActionGrid.E;
    }
}
