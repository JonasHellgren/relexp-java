package chapters.ch13.factory.jumper;

import chapters.ch13.domain.searcher.core.SearcherParameters;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactorySearcherParametersJumper {


    public static SearcherParameters test() {
        return SearcherParameters.builder()
                .uctExploration(1.0)
                .maxTreeDepth(5)
                .maxDepth(500).maxIterations(100)
                .discountNormal(1.0).learningRateNormal(1.0)
                .discountDefensive(0.5).learningRateDefensive(0.1)
                .build();
    }


    public static SearcherParameters runner(int maxIterations,
                                            RunnerSettings runnerSettings) {
        return test()
                .withMaxIterations(maxIterations)
                .withUctExploration(runnerSettings.uctExploration())
                .withDiscountDefensive(runnerSettings.discountDefensive())
                .withLearningRateDefensive(runnerSettings.learningRateDefensive());
    }

}
