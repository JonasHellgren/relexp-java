package chapters.ch8.domain.trainer.core;

import chapters.ch8.domain.agent.core.ExperienceParking;
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
            int t = s.nSteps();  //nof steps taken so far
            double probRandom = d.probRandom(t);
            double lr = d.learningRate(t);
            sr = d.step(s, a);
            var stateNew = sr.stateNew();
            var aNew = d.chooseAction(stateNew, probRandom);
            var exp = ExperienceParking.of(s, a, sr, aNew, stats.rewardAverage());
            double tdError = d.updateAgentMemory(exp, lr);  //Update memory at s and a
            s = stateNew.copy();
            a = aNew.copy();  //for next t
            double dar = calculateDeltaAvgReward(t, tdError);
            stats.update(sr.reward(), dar, s.nOccupied());
            recorder.add(MeasuresParkingTraining.getMeasures(s, stats));
        } while (!sr.isTerminal());

    }

    public void logTimer() {
        log.info("Finished training, in time (seconds) = " + dependencies.timer().timeInSecondsAsString());
    }


    private double calculateDeltaAvgReward(int step, double tdError) {
        double lrRewardAvg = dependencies.learningRateAvgReward(step);
        return lrRewardAvg * tdError;
    }

}
