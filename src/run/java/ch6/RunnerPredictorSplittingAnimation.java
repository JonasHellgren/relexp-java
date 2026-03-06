package ch6;

import chapters.ch6.domain.trainers.state_predictor.TrainerStatePredictor;
import chapters.ch6.implem.factory.TrainerDependenciesFactorySplitting;
import chapters.ch6.implem_animation.AnimationSplit;
import core.foundation.config.ConfigFactory;

public class RunnerPredictorSplittingAnimation {

    static final int N_STEPS_HORIZON = 3;
    static final int N_EPISODES = 50;
    static final double LEARNING_RATE_START = 0.1;

    public static void main(String[] args) {
        var trainer = defineTrainer(N_STEPS_HORIZON);
        var cfg= ConfigFactory.getAnimationConfig();
        var animation = AnimationSplit.create(trainer.getDependencies().environment(), cfg);
        trainer.train(animation);
    }

    private static TrainerStatePredictor defineTrainer(int nStepsHorizon) {
        var dependencies = TrainerDependenciesFactorySplitting.givenRandomPolicySplitting(
                nStepsHorizon, N_EPISODES, LEARNING_RATE_START);
        return TrainerStatePredictor.of(dependencies);
    }

}
