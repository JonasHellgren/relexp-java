package chapters.ch6._shared.episode_generator;

import chapters.ch4.domain.trainer.core.ExperienceGrid;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.cond.Conditionals;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class EpisodeGeneratorGrid {

    TrainerDependenciesMultiStep dependencies;

    public static EpisodeGeneratorGrid of(TrainerDependenciesMultiStep dependencies) {
        return new EpisodeGeneratorGrid(dependencies);
    }

    public List<ExperienceGrid> generate(double probRandom) {
        List<ExperienceGrid> experiences = new ArrayList<>();
        var state = dependencies.startStateSupplier().getStartState();
        var environment = dependencies.environment();
        var agent= dependencies.agent();
        var action = agent.chooseAction(state,probRandom);
        var counter= Counter.ofMaxCount(dependencies.trainerParameters().nStepsMax());
        boolean isTerminal;
        do {
            var sr = environment.step(state, action);
            var stateAfterStep = sr.sNext();
            var actionNext = agent.chooseAction(stateAfterStep,probRandom);
            experiences.add(ExperienceGrid.ofSarsa(state, action, sr,actionNext));
            action=actionNext;
            state=stateAfterStep;
            isTerminal = sr.isTerminal();
            counter.increase();
        } while (!isTerminal && counter.isNotExceeded());
        Conditionals.executeIfTrue(counter.isExceeded(),() -> log.info("nof steps exceeded"));
        return experiences;
    }

}
