package ch7;


import chapters.ch7.domain.trainer.TrainerOneStepTdQLearningWithSafety;
import chapters.ch7.factory.SafetyLayerFactoryTreasure;
import chapters.ch7.factory.TrainerDependencySafeFactory;
import core.foundation.config.ConfigFactory;

import static chapters.ch7.plotter.ChartPlotterSafe.showAndSavePlots;

public class RunnerHardCodedFail {

    public static final int N_EPISODES = 10_000;
    public static final double LEARNING_RATE_START = 0.1;
    public static final double PROB_RAND_START = 0.9;

    public static void main(String[] args) {
        var dependencies = TrainerDependencySafeFactory.treasure(N_EPISODES, LEARNING_RATE_START, PROB_RAND_START);
        var safetyLayer = SafetyLayerFactoryTreasure.produce(dependencies);
        var trainer = TrainerOneStepTdQLearningWithSafety.givenSafetyLayerOf(dependencies, safetyLayer);
        trainer.train();
        trainer.logTrainingTime();
        showAndSavePlots(trainer,
                "safe_hard_codedfails",
                ConfigFactory.plotConfig(),
                ConfigFactory.pathPicsConfig().ch7());
    }

}
