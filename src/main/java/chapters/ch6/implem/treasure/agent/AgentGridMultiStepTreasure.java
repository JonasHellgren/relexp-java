package chapters.ch6.implem.treasure.agent;

import core.gridrl.ActionSelectorGrid;
import core.gridrl.StateActionMemoryGrid;
import core.gridrl.StateActionGrid;
import core.gridrl.AgentGridParameters;
import chapters.ch4.implem.treasure.core.EnvironmentParametersTreasure;
import chapters.ch4.implem.treasure.core.InformerTreasure;
import chapters.ch6.domain.agent.core.AgentGridMultiStepI;
import chapters.ch6.domain.trainer.multisteps_after_episode.MultiStepResultGrid;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Implementation of the AgentGridMultiStepI interface for the treasure environment.
 * This agent uses a memory grid to store state-action values and an action selector to choose actions.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentGridMultiStepTreasure implements AgentGridMultiStepI {

    private  static final int ZERO_PROBABILITY = 0;
    private final StateActionMemoryGrid memory;
    private final ActionSelectorGrid actionSelector;

    public static AgentGridMultiStepTreasure of(AgentGridParameters agentParameters,
                                                EnvironmentParametersTreasure envParams) {
        var informer= InformerTreasure.create(envParams);

        return new AgentGridMultiStepTreasure(
                StateActionMemoryGrid.of(agentParameters,informer),
                ActionSelectorGrid.of(informer));
    }

    @Override
    public StateActionMemoryGrid getMemory() {
        return memory;
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
