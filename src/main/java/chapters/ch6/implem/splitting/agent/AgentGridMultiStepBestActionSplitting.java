package chapters.ch6.implem.splitting.agent;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.implem.splitting_path_problem.InformerSplitting;
import core.gridrl.StateActionMemoryGrid;
import core.gridrl.StateActionGrid;
import core.gridrl.AgentGridParameters;
import chapters.ch6.domain.agent.AgentGridMultiStepI;
import chapters.ch6.domain.trainer_dep.result_generator.MultiStepResultGrid;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AllArgsConstructor;

/**
 * Implementation of the AgentGridMultiStepI interface for the splitting agent.
 * This agent chooses the best action (rule based) based on the current state and updates its memory accordingly.
 */
@AllArgsConstructor
public class AgentGridMultiStepBestActionSplitting implements AgentGridMultiStepI {

    public static final ActionGrid DUMMY_ACTION = ActionGrid.E;
    public static final double DUMMY_PROB = 1.0;
    private final StateActionMemoryGrid memory;

    public static AgentGridMultiStepBestActionSplitting of(AgentGridParameters agentParameters,
                                                           EnvironmentParametersSplitting gridParameters) {
        return new AgentGridMultiStepBestActionSplitting(
                StateActionMemoryGrid.of(agentParameters, InformerSplitting.create(gridParameters)));
    }

    @Override
    public StateActionMemoryGrid getMemory() {
        return memory;
    }

    @Override
    public ActionGrid chooseAction(StateGrid s, double probRandom) {
        return s.equals(StateGrid.of(2,1))
                ? ActionGrid.N
                : ActionGrid.E;
    }

    @Override
    public ActionGrid chooseActionNoExploration(StateGrid s) {
        return chooseAction(s, DUMMY_PROB);
    }

    @Override
    public void fit(MultiStepResultGrid ms, double learningRate) {
        double valueTar=calculateValueTarget(ms);
        memory.fit(ms.state(), DUMMY_ACTION, valueTar, learningRate);
    }

    @Override
    public double calculateValueTarget(MultiStepResultGrid ms) {
        double valFutureState=ms.isStateFuturePresent()
                ? read(ms.stateFuture().orElseThrow())
                :0;
        return ms.sumRewards()+valFutureState;
    }

    @Override
    public double read(StateActionGrid sa) {
        return read(sa.state());
    }

    @Override
    public double read(StateGrid s) {
        var sa=StateActionGrid.of(s, DUMMY_ACTION);
        return memory.read(sa);
    }

    @Override
    public double read(StateGrid s, ActionGrid a) {
        return read(StateActionGrid.of(s,a));
    }
}
