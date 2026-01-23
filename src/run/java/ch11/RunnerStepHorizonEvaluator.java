package ch11;

import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.trainer.core.TrainerLunarMultiStep;
import chapters.ch11.factory.DependencyFactory;
import chapters.ch11.factory.LunarEnvParamsFactory;
import chapters.ch11.plotting.ChangingHorizonPlotter;
import core.foundation.util.collections.ListCreatorUtil;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import lombok.extern.java.Log;
import java.util.List;
import static core.foundation.util.collections.ListUtil.findAverage;

@Log
public class RunnerStepHorizonEvaluator {
    public static final int N_EPISODES = 5000; //5000
    public static final int N_EVALS_PER_N = 10;
    public static final int N_MAX = 20;
    static final List<Integer> N_STEPS_LIST = ListCreatorUtil.createFromStartWithStepWithNofItems(1,1,N_MAX);

    public static void main(String[] args) {
        var ep = LunarEnvParamsFactory.produceDefault();
        var results = evaluateNSteps(ep, N_STEPS_LIST);
        ChangingHorizonPlotter.plotNStepResults(results, N_STEPS_LIST);
    }

    static ChangingHorizonPlotter.ResultsNStep evaluateNSteps(LunarParameters ep, List<Integer> nStepsList) {
        var results = ChangingHorizonPlotter.ResultsNStep.empty();
        for (var nSteps : nStepsList) {
            log.info("nSteps: " + nSteps);
            for (int i = 0; i < N_EVALS_PER_N; i++) {
                var dependencies = DependencyFactory.produce(ep, nSteps, N_EPISODES);
                var trainer = TrainerLunarMultiStep.of(
                        dependencies);
                trainer.train();
                var sumRewardsList = trainer.getRecorder().trajectory(ProgressMeasureEnum.RETURN);
                double sumRewardAverage = findAverage(sumRewardsList).orElseThrow();
                results.add(nSteps, sumRewardAverage);
            }
        }
        return results;
    }
}
