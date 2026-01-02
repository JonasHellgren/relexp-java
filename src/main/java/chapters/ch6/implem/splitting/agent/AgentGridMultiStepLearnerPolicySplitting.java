package chapters.ch6.implem.splitting.agent;

import chapters.ch4.domain.helper.ActionSelectorGrid;
import chapters.ch4.domain.memory.MemoryGrid;
import chapters.ch4.domain.memory.StateActionGrid;
import chapters.ch4.domain.param.AgentGridParameters;
import chapters.ch6.domain.agent.core.AgentGridMultiStepI;
import chapters.ch6.domain.trainer.multisteps_after_episode.MultiStepResultGrid;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridParametersI;
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
    private final MemoryGrid memory;
    private final ActionSelectorGrid actionSelector;

    public static AgentGridMultiStepLearnerPolicySplitting of(AgentGridParameters agentParameters,
                                                              EnvironmentGridParametersI envParameters) {
        return new AgentGridMultiStepLearnerPolicySplitting(
                MemoryGrid.of(agentParameters,envParameters),
                ActionSelectorGrid.of(envParameters));
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
