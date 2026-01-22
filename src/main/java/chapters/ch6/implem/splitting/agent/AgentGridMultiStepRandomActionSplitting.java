package chapters.ch6.implem.splitting.agent;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.implem.splitting_path_problem.InformerSplitting;
import core.gridrl.StateActionMemoryGrid;
import core.gridrl.AgentGridParameters;
import core.foundation.util.rand.RandUtil;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;

/**
 * Implementation of the AgentGridMultiStepI interface for the splitting agent.
 * This agent chooses the best action (rule based) based on the current state and updates its memory accordingly.
 */

public class AgentGridMultiStepRandomActionSplitting extends AgentGridMultiStepBestActionSplitting {

    public AgentGridMultiStepRandomActionSplitting(StateActionMemoryGrid memory) {
        //super(gridParameters, memory);
        super(memory);
    }

    public static AgentGridMultiStepRandomActionSplitting of(AgentGridParameters agentParameters,
                                                             EnvironmentParametersSplitting gridParameters) {
        var informer= InformerSplitting.create(gridParameters);
        return new AgentGridMultiStepRandomActionSplitting(StateActionMemoryGrid.of(agentParameters, informer));
    }


    @Override
    public ActionGrid chooseAction(StateGrid s, double probRandom) {
        return s.equals(StateGrid.of(2, 1))
                ? nOrS()
                : ActionGrid.E;
    }

    private static ActionGrid nOrS() {
        return RandUtil.randomNumberBetweenZeroAndOne() < 0.5 ? ActionGrid.N : ActionGrid.S;
    }


}
