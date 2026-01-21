package chapters.ch6.implem.splitting.agent;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.implem.splitting_path_problem.InformerSplitting;
import core.gridrl.ActionSelectorGrid;
import core.gridrl.StateActionMemoryGrid;
import core.gridrl.StateActionGrid;
import core.gridrl.AgentGridParameters;
import chapters.ch6.domain.agent.AgentGridMultiStepI;
import chapters.ch6.domain.trainer.result_generator.MultiStepResultGrid;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Implementation of a multi-step learner policy splitting agent.
 * This agent uses a memory grid to store state-action values and an action selector to choose actions.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentGridMultiStepLearnerPolicySplitting implements AgentGridMultiStepI {

    private  static final int ZERO_PROBABILITY = 0;
    @Getter
    private final StateActionMemoryGrid memory;
    private final ActionSelectorGrid actionSelector;

    public static AgentGridMultiStepLearnerPolicySplitting of(AgentGridParameters agentParameters,
                                                              EnvironmentParametersSplitting envParameters) {
        return new AgentGridMultiStepLearnerPolicySplitting(
                StateActionMemoryGrid.of(agentParameters, InformerSplitting.create(envParameters)),
                ActionSelectorGrid.of(InformerSplitting.create(envParameters)));
    }

    @Override
    public ActionGrid chooseAction(StateGrid s, double probRandom) {
        return actionSelector.chooseActionEpsilonGreedy(s, probRandom, memory);
    }

    @Override
    public ActionGrid chooseActionNoExploration(StateGrid s) {
        return chooseAction(s, ZERO_PROBABILITY);
    }

    @Override
    public void fit(MultiStepResultGrid ms, double learningRate) {
        double valueTar = calculateValueTarget(ms);
        memory.fit(StateActionGrid.of(ms.state(),ms.action()), valueTar, learningRate);
    }

    @Override
    public double calculateValueTarget(MultiStepResultGrid ms) {
        double valFutureState= ms.isStateFuturePresent()
                ? memory.read(ms.stateFuture().orElseThrow(), ms.actionFuture().orElseThrow())
                :0;
        return ms.sumRewards()+valFutureState;
    }

    @Override
    public double read(StateActionGrid sa) {
        return memory.read(sa);
    }

    @Override
    public double read(StateGrid s) {
        var bestAction=chooseActionNoExploration(s);
        return memory.read(StateActionGrid.of(s,bestAction));
    }

    @Override
    public double read(StateGrid s, ActionGrid a) {
        return read(StateActionGrid.of(s,a));
    }

}
