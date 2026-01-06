package chapters.ch3.policies;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.implem.splitting_path_problem.InformerSplitting;
import core.foundation.util.rand.RandUtils;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SplittingPathPolicyRandom implements SplittingPathPolicyI {

    EnvironmentParametersSplitting parameters;

    public static SplittingPathPolicyRandom of(EnvironmentParametersSplitting parameters) {
        return new SplittingPathPolicyRandom(parameters);
    }

    @Override
    public ActionGrid chooseAction(StateGrid s) {
        var informer= InformerSplitting.create(parameters);
        boolean isAtSplit=informer.isAtSplit(s);
        var northOrEast= RandUtils.randomNumberBetweenZeroAndOne() > 0.5
                ? ActionGrid.N
                : ActionGrid.S;
            return (isAtSplit)
                ? northOrEast
                : ActionGrid.E;
    }
}
