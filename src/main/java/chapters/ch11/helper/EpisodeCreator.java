package chapters.ch11.helper;

import chapters.ch11.domain.environment.core.StepReturnLunar;
import chapters.ch11.domain.trainer.core.ExperienceLunar;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import com.beust.jcommander.internal.Lists;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.cond.Conditionals;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import java.util.List;

/**
 * A utility class responsible for creating episodes in the lunar lander environment.
 * An step is a sequence of experiences, where each experience consists of a state, an action, and a reward.
 */

@AllArgsConstructor
@Log
public class EpisodeCreator {

    private final TrainerDependencies dependencies;

    public static EpisodeCreator of(TrainerDependencies dependencies) {
        return new EpisodeCreator(dependencies);
    }

    public List<ExperienceLunar> create() {
        return getExperiencesWithFlag(true);
    }

    public List<ExperienceLunar> createNotExploring() {
        return getExperiencesWithFlag(false);
    }

    private List<ExperienceLunar> getExperiencesWithFlag(boolean isExploring) {
        List<ExperienceLunar> experienceList = Lists.newArrayList();
        var agent=dependencies.agent();
        var stateStart=dependencies.startState();
        var counter= Counter.ofMaxCount(dependencies.trainerParameters().nStepsMax());
        var environment=dependencies.environment();
        StepReturnLunar sr=StepReturnLunar.ofNotFailAndNotTerminal();
        var state=stateStart.copy();
        while (sr.isNotTerminalAndNofStepsNotExceeded(counter)) {
            var action = (isExploring) ? agent.chooseAction(state) : agent.chooseActionNoExploration(state);
            sr = environment.step(state, action);
            experienceList.add(ExperienceLunar.of(state, action, sr));
            state=sr.stateNew();
            counter.increase();
        }
        Conditionals.executeIfTrue(counter.isExceeded(), () -> log.info("Nof steps exceeded"));
        return experienceList;
    }

}
