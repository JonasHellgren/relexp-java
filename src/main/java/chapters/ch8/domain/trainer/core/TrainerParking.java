package chapters.ch8.domain.trainer.core;


import chapters.ch8.domain.agent.core.AgentParking;
import chapters.ch8.domain.agent.core.ExperienceParking;
import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.environment.core.StepReturnParking;
import chapters.ch8.plotting.MeasuresParkingTraining;
import chapters.ch8.plotting.RecorderTrainerParking;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class TrainerParking {

    private final TrainerDependenciesParking dependencies;
    @Getter
    private final RecorderTrainerParking recorder;

    public static TrainerParking of(TrainerDependenciesParking dependencies) {
        return new TrainerParking(dependencies, RecorderTrainerParking.empty());
    }

    public void train() {
        var d = dependencies;
        var s = d.startStateSupplier().state();
        var a = d.chooseAction(s, d.probRandom(s.nSteps()));
        var stats = TrainingStats.empty();
        var sr = StepReturnParking.empty();
        do {
            int step = s.nSteps();
            double probRandom = d.probRandom(step);
            double lr = d.learningRate(step);
            sr = d.step(s, a);
            var stateNew = sr.stateNew();
            var aNew = d.chooseAction(stateNew, probRandom);
            var exp = ExperienceParking.of(s, a, sr, aNew, stats.rewardAverage());
            double tdError = d.updateAgentMemory(exp, lr);  //Update memory at s and a
            s = stateNew.copy();
            a = aNew.copy();
            double lrRewardAvg = d.learningRateAvgReward(step);
            stats.update(sr.reward(), lrRewardAvg * tdError, s.nOccupied());
            recorder.add(MeasuresParkingTraining.getMeasures(s, stats));
        } while (!sr.isTerminal());

    }

    public void logTimer() {
        log.info("Finished training, in time (seconds) = " + dependencies.timer().timeInSecondsAsString());
    }
}
