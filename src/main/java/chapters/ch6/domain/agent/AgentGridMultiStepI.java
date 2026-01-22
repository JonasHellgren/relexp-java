package chapters.ch6.domain.agent;


import core.gridrl.StateActionMemoryGrid;
import core.gridrl.StateActionGrid;
import chapters.ch6.domain.trainer_dep.result_generator.MultiStepResultGrid;
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
