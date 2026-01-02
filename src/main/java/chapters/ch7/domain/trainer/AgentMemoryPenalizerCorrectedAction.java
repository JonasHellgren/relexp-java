package chapters.ch7.domain.trainer;

import chapters.ch4.domain.agent.AgentQLearningGrid;
import chapters.ch4.domain.memory.StateActionGrid;
import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * This class is responsible for penalizing actions in the agent's memory.
 * It takes a list of experiences and updates the memory accordingly.
 * Corrected action => penalize corresponding state-action
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentMemoryPenalizerCorrectedAction {

    TrainerGridDependencies dependencies;

    public static AgentMemoryPenalizerCorrectedAction of(TrainerGridDependencies dependencies) {
        return new AgentMemoryPenalizerCorrectedAction(dependencies);
    }

    /**
     * Penalizes the actions in the given experiences and updates the agent's memory.
     *
     * @param experiences the list of experiences to penalize
     */
    public void penalize(List<ExperienceGridCorrectedAction> experiences) {
        var agent= (AgentQLearningGrid) dependencies.agent();
        var memory=agent.getMemory();
        for (ExperienceGridCorrectedAction e : experiences) {
            if (e.isCorrected()) {
                var sa= StateActionGrid.of(e.experienceGrid().state(), e.experienceGrid().action());
                double valueOld=memory.read(sa);
                memory.write(sa, valueOld+dependencies.trainerParameters().penaltyActionCorrection());
            }
        }
    }


}
