package chapters.ch6.domain.trainers.during_episode;

import chapters.ch6.domain.trainer_dep.core.TrainerDependenciesMultiStep;
import core.foundation.util.cond.ConditionalsUtil;
import core.gridrl.ExperienceGrid;
import core.gridrl.StateActionGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExperienceListCreator {

    private final TrainerDependenciesMultiStep dependencies;
        private final MultiStepMemoryUpdater memoryUpdater;

    public static ExperienceListCreator of(TrainerDependenciesMultiStep dependencies) {
        return new ExperienceListCreator(dependencies, MultiStepMemoryUpdater.of(dependencies));
    }

    public List<ExperienceGrid> createExperiences(int i) {
        List<ExperienceGrid> experiences = new ArrayList<>();
        var agent = dependencies.agent();
        var environment = dependencies.environment();
        double probRandom = dependencies.calcProbRandomActiont(i);
        var startState = dependencies.getStartState();
        var sa= StateActionGrid.of(startState,agent.chooseAction(startState, probRandom));
        boolean isTerminal;
        dependencies.stepCounter().reset();
        do {
            var sr = environment.step(sa.state(), sa.action());
            var stateAfterStep = sr.sNext();
            var actionNext = agent.chooseAction(stateAfterStep, probRandom);
            experiences.add(ExperienceGrid.ofSarsa(sa.state(), sa.action(), sr, actionNext));
            int tau = dependencies.stepCounter().getCount()- dependencies.backupHorizon();
            ConditionalsUtil.executeIfTrue(tau >= 0, () ->
                    memoryUpdater.updateAgentMemory(tau, experiences, dependencies.calcLearningRatet(i)));
            sa=StateActionGrid.of(stateAfterStep, actionNext);
            isTerminal = sr.isTerminal();
            dependencies.stepCounter().increase();
        } while (!isTerminal && dependencies.stepCounter().isNotExceeded());
        return experiences;
    }

}
