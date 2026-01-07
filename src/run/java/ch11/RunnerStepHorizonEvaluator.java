package ch11;

import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.trainer.core.TrainerLunarMultiStep;
import core.foundation.util.collections.ListCreator;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import lombok.extern.java.Log;
import java.util.List;
import static ch11.RunnerHelper.getDependencies;
import static ch11.RunnerHelper.plotNStepResults;
import static core.foundation.util.collections.MyListUtils.findAverage;

@Log
public class RunnerStepHorizonEvaluator {
    public static final int N_EPISODES = 5_000;
    public static final int N_EVALS_PER_N = 10;
    public static final int N_MAX = 20;
    static final List<Integer> N_STEPS_LIST = ListCreator.createFromStartWithStepWithNofItems(1,1,N_MAX);

    public static void main(String[] args) {
        var ep = LunarParameters.defaultProps();
        var results = evaluateNSteps(ep, N_STEPS_LIST);
        plotNStepResults(results, N_STEPS_LIST);
    }

    static RunnerHelper.ResultsNStep evaluateNSteps(LunarParameters ep, List<Integer> nStepsList) {
        var results = RunnerHelper.ResultsNStep.empty();
        for (var nSteps : nStepsList) {
            log.info("nSteps: " + nSteps);
            for (int i = 0; i < N_EVALS_PER_N; i++) {
                var trainer = TrainerLunarMultiStep.of(getDependencies(ep, nSteps, N_EPISODES));
                trainer.train();
                var sumRewardsList = trainer.getRecorder().trajectory(ProgressMeasureEnum.RETURN);
                double sumRewardAverage = findAverage(sumRewardsList).orElseThrow();
                results.add(nSteps, sumRewardAverage);
            }
        }
        return results;
    }
}
