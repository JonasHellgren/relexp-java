package ch6;

import chapters.ch4.domain.animation.AnimationGridI;
import chapters.ch6.domain.trainers.state_predictor.TrainerStatePredictor;
import chapters.ch6.implem.factory.TrainerDependenciesFactorySplitting;
import chapters.ch6.implem_animation.AnimationSplit;
import core.foundation.config.ConfigFactory;
import core.gridrl.StateGrid;

public class RunnerPredictorSplittingAnimation {

    public static final int N_STEPS_HORIZON = 4;

    public static final int N_EPISODES = 20;
    public static final double TOL = 0.3;
    public static final double LEARNING_RATE_START = 0.1;


    public static void main(String[] args) {
        var trainer = defineTrainer(N_STEPS_HORIZON);
        //trainer.train();
        var cfg= ConfigFactory.getAnimationConfig();
        trainer.train(AnimationSplit.create(trainer.getDependencies().environment(),cfg));

        var agent= trainer.getDependencies().agent();
        double value01= agent.read(StateGrid.of(0,1));

        System.out.println("value01 = " + value01);
    }


    private static TrainerStatePredictor defineTrainer(int nStepsHorizon) {
        var dependencies = TrainerDependenciesFactorySplitting.givenRandomPolicySplitting(
                nStepsHorizon, N_EPISODES, LEARNING_RATE_START);
        return TrainerStatePredictor.of(dependencies);
    }


}
