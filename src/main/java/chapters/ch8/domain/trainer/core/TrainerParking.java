package chapters.ch8.domain.trainer.core;


import chapters.ch8.domain.agent.core.ExperienceParking;
import chapters.ch8.plotting.MeasuresParkingTraining;
import chapters.ch8.plotting.RecorderTrainerParking;
import core.foundation.gadget.timer.CpuTimer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class TrainerParking {

    public static final int PROB_RANDOM_START = 1;
    private final TrainerDependenciesParking dependencies;
    @Getter
    private final RecorderTrainerParking recorder;

    public static TrainerParking of(TrainerDependenciesParking dependencies) {
        return new TrainerParking(dependencies, RecorderTrainerParking.empty());
    }

    public void train() {
        var agent = dependencies.agent();
        var environment = dependencies.environment();
        var s = dependencies.startStateSupplier().state();
        var a = agent.chooseAction(s, PROB_RANDOM_START);
        double rewardAverage = 0;
        var helper=TrainerHelper.empty();
        while (!helper.termState) {
            int step = s.nSteps();
            double probRandom = dependencies.probRandom(step);
            double lr = dependencies.learningRate(step);
            double lrRewardAvg = dependencies.lrRewardAverage(step);
            var sr = environment.step(s, a);
            var stateNew = sr.stateNew();
            var aNew = agent.chooseAction(stateNew, probRandom);
            var exp = ExperienceParking.of(s, a, sr, aNew, rewardAverage);
            double tdError = agent.fitMemory(exp, lr);  //Update memory at s and a
            rewardAverage = rewardAverage + lrRewardAvg * tdError;
            s = stateNew.copy();
            a = aNew.copy();

            helper.update(sr,s.nOccupied());
            recorder.add(MeasuresParkingTraining.builder()
                    .step(s.nSteps()).sumRewards(helper.accum.value()).rewardAverage(rewardAverage)
                    .nOoccupAvg(helper.stats.getMean())
                    .build());
        }
    }

    public void logTimer() {
        log.info("Finished training, in time (seconds) = " + dependencies.timer().timeInSecondsAsString());
    }
}
