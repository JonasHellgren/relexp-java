package ch11;

import chapters.ch11.animation.AnimationLunar;
import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.core.TrainerLunarMultiStep;
import chapters.ch11.factory.DependencyFactory;
import chapters.ch11.factory.LunarEnvParamsFactory;
import core.foundation.config.ConfigFactory;
import lombok.SneakyThrows;

public class RunnerTrainerLunarAnimation {

    public static final int STEP_HORIZON = 5;
    public static final int N_EPISODES = 10_000;

    @SneakyThrows
    public static void main(String[] args) {
        var ep = LunarEnvParamsFactory.produceDefault().withDt(0.1);
        var trainerDependencies = DependencyFactory.produce(ep, STEP_HORIZON, N_EPISODES);
        var trainer = TrainerLunarMultiStep.of(trainerDependencies);
        var cfg= ConfigFactory.getAnimationConfig();
        trainer.train(AnimationLunar.create(cfg, getEnv(trainerDependencies)));
    }

    private static EnvironmentLunar getEnv(TrainerDependencies trainerDependencies) {
        return (EnvironmentLunar) trainerDependencies.environment();
    }


}
