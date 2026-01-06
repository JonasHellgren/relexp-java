package core.gridrl;


import chapters.ch4.domain.trainer.TrainerGridParameters;
import core.foundation.gadget.timer.CpuTimer;
import core.foundation.util.math.LogarithmicDecay;
import core.learninggadgets.LrAndProbRandDecay;
import core.learninggadgets.StepAndEpisCounter;
import core.plotting.progress_plotting.ProgressMeasures;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the dependencies required for a grid world trainer.
 * This includes the agent, environment, trainer parameters, and start
 * state supplier.
 */

@With
public record TrainerGridDependencies(
         AgentGridI agent,
         EnvironmentGridI environment,
         TrainerGridParameters trainerParameters,
         StartStateGridSupplierI startStateSupplier,
         InformerGridParamsI informerParams,
         LrAndProbRandDecay lrAndProbRandDecay,
         StepAndEpisCounter counters,
         CpuTimer timer,
         List<ExperienceGrid>experiences

) {

    public static TrainerGridDependencies of(AgentGridI agent,
                                             EnvironmentGridI environment,
                                             TrainerGridParameters trainerParameters,
                                             StartStateGridSupplierI startStateSupplier,
                                             InformerGridParamsI informerParams) {
    return new TrainerGridDependencies(agent,
            environment,
            trainerParameters,
            startStateSupplier,
            informerParams,
            LrAndProbRandDecay.of(trainerParameters),
            StepAndEpisCounter.of(trainerParameters),
            CpuTimer.empty(),
            new ArrayList<>());
    }

    public StateGrid getStartState() {
        return startStateSupplier.getStartState();
    }

    public boolean isTerminal(StateGrid s) {
        return informerParams().isTerminal(s);
    }

    public int getNofEpisodes() {
        return trainerParameters().nEpisodes();
    }

    public Integer maxNofSteps() {
        return trainerParameters().nStepsMax();
    }

    public boolean isCorrectEnvironmentNamesForParameters() {
        String name = environment.name();
        return agent.getAgentParameters().environmentName().equals(name) &&
                trainerParameters.environmentName().equals(name) &&
                startStateSupplier.environmentName().equals(name);
    }

    public LogarithmicDecay lrDecay() {
        return lrAndProbRandDecay.lrDecay();
    }

    public LogarithmicDecay probRandomDecay() {
        return lrAndProbRandDecay.probDecay();
    }

    public ActionGrid chooseAction(StateGrid s, int ei) {
        return agent().chooseAction(s, probRandomDecay().calcOut(ei));
    }


    public void updateAgentMemoryFromExperience(ExperienceGrid e, int ei) {
        agent().fitMemory(e, lrDecay().calcOut(ei));
    }

    public  boolean notTerminalStateAndNotToManySteps(StateGrid s) {
        return !isTerminal(s) && counters.stepCounter().isNotExceeded();
    }

    public void increaseStepCounter() {
        counters.stepCounter().increase();
    }


    public StepReturnGrid takeAction(StateGrid s, ActionGrid action) {
        return environment().step(s, action);
    }


    public void resetBeforeEpisode() {
        counters.stepCounter().reset();
        experiences.clear();
    }

    public void clearTimer() {
        timer.reset();
    }

    public void saveExperienceForRecording(ExperienceGrid e) {
        experiences.add(e);
    }

    public ProgressMeasures getProgressMeasures() {
        var factory= ProgressMeasuresExtractorGrid.of(this);
        return factory.produce(experiences);
    }


}
