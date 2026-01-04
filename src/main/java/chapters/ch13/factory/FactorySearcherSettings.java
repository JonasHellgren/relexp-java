package chapters.ch13.factory;

import k_mcts.domain.searcher.settings.SearcherSettings;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactorySearcherSettings {



    public static SearcherSettings forTestClimber() {
        return SearcherSettings.builder()
                .uctExploration(1.0)
                .maxTreeDepth(5)
                .maxDepth(500).maxIterations(100)
                .discountNormal(1.0).learningRateNormal(1.0)
                .discountDefensive(0.5).learningRateDefensive(0.1)
                .build();
    }

    public static SearcherSettings forTestLane() {
        return SearcherSettings.builder()
                .uctExploration(10.0)  //2  or 10
                .maxTreeDepth(3*4+4)  //4 steps per sec, max 3 sec
                .maxDepth(500).maxIterations(50_000)
                .discountNormal(1.0).learningRateNormal(1.0)
                //.discountNormal(1.0).learningRateNormal(0.1)
                .discountDefensive(0.5).learningRateDefensive(0.1)
                //.discountDefensive(0.5).learningRateDefensive(0.01)
                .build();
    }


    public record RunnerSettings(
            double uctExploration,
            double discountDefensive,
            double learningRateDefensive
    ) {

        public static RunnerSettings of(double uctExploration,
                                        double discountDefensive,
                                        double learningRateDefensive) {
            return new RunnerSettings(uctExploration,discountDefensive,learningRateDefensive);
        }

    }

    public static SearcherSettings forRunnerClimber(int maxIterations,
                                                    RunnerSettings runnerSettings) {
        return forTestClimber()
                .withMaxIterations(maxIterations)
                .withUctExploration(runnerSettings.uctExploration())
                .withDiscountDefensive(runnerSettings.discountDefensive())
                .withLearningRateDefensive(runnerSettings.learningRateDefensive());
    }
}
