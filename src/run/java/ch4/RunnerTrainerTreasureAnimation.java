package ch4;


import chapters.ch4.domain.trainer.TrainerOneStepTdQLearning;
import chapters.ch4.implem.treasure.factory.TreasureRunnerFactory;
import chapters.ch4.implem_animation.AnimationTreasure;
import core.foundation.config.ConfigFactory;


public class RunnerTrainerTreasureAnimation {
    public static final int NOF_DIGITS = 0;

    public static void main(String[] args) {
        var dep=TreasureRunnerFactory.produceDependencies().lowExploration();
        var trainer= TrainerOneStepTdQLearning.of(dep);
        var env=dep.environment();
        var animConfig=ConfigFactory.getAnimationConfig();
        trainer.train(AnimationTreasure.create(env,animConfig));
    }

}
