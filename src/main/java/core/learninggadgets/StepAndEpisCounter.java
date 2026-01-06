package core.learninggadgets;

import chapters.ch4.domain.trainer.TrainerGridParameters;
import core.foundation.gadget.cond.Counter;

public record StepAndEpisCounter(
        Counter stepCounter,
        Counter episCounter
) {

    public static StepAndEpisCounter of(Counter stepCounter, Counter episCounter) {
        return new StepAndEpisCounter(stepCounter, episCounter);
    }

    public static StepAndEpisCounter of(TrainerGridParameters trainerParameters) {
        return of(
                Counter.ofMaxCount(trainerParameters.nStepsMax()),
                Counter.ofMaxCount(trainerParameters.nEpisodes()));
    }

    public int nEpisodes() {
        return episCounter.getCount();
    }

    public int nSteps() {
        return stepCounter.getCount();
    }

    public boolean isStepsNotExceeded() {
        return stepCounter.isNotExceeded();
    }

    public boolean isEpisNotExceeded() {
        return episCounter.isNotExceeded();
    }
}
