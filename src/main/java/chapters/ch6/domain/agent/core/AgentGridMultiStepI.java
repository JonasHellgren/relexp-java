package chapters.ch6.domain.agent.core;


import chapters.ch4.domain.memory.StateActionMemoryGrid;
import chapters.ch4.domain.memory.StateActionGrid;
import chapters.ch6.domain.trainer.multisteps_after_episode.MultiStepResultGrid;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;

public interface AgentGridMultiStepI {
    StateActionMemoryGrid getMemory();
    ActionGrid chooseAction(StateGrid s, double probRandom);
    ActionGrid chooseActionNoExploration(StateGrid s);
    void fit(MultiStepResultGrid ms, double learningRate);
    double calculateValueTarget(MultiStepResultGrid ms);
    double read(StateActionGrid sa);
    double read(StateGrid s);
    double read(StateGrid s, ActionGrid a);
}
