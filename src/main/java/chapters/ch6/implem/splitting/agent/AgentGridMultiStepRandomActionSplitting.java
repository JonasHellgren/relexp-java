package chapters.ch6.implem.splitting.agent;

import chapters.ch4.domain.memory.MemoryGrid;
import chapters.ch4.domain.param.AgentGridParameters;
import core.foundation.util.rand.RandUtils;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridParametersI;
import core.gridrl.StateGrid;

/**
 * Implementation of the AgentGridMultiStepI interface for the splitting agent.
 * This agent chooses the best action (rule based) based on the current state and updates its memory accordingly.
 */

public class AgentGridMultiStepRandomActionSplitting extends AgentGridMultiStepBestActionSplitting {

    public AgentGridMultiStepRandomActionSplitting(EnvironmentGridParametersI gridParameters, MemoryGrid memory) {
        super(gridParameters, memory);
    }

    public static AgentGridMultiStepRandomActionSplitting of(AgentGridParameters agentParameters,
                                                             EnvironmentGridParametersI gridParameters) {
        return new AgentGridMultiStepRandomActionSplitting(gridParameters, MemoryGrid.of(agentParameters, gridParameters));
    }


    @Override
    public ActionGrid chooseAction(StateGrid s, double probRandom) {
        return s.equals(StateGrid.of(2, 1))
                ? nOrS()
                : ActionGrid.E;
    }

    private static ActionGrid nOrS() {
        return RandUtils.randomNumberBetweenZeroAndOne() < 0.5 ? ActionGrid.N : ActionGrid.S;
    }


}
