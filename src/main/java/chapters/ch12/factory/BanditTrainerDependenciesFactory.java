package chapters.ch12.factory;

import chapters.ch10.bandit.domain.environment.EnvironmentParametersBandit;
import chapters.ch12.domain.bandit.environment.EnvironmentBanditWrapper;
import chapters.ch12.domain.bandit.trainer.BanditActionValueMemory;
import chapters.ch12.domain.bandit.trainer.BanditTrainerDependencies;
import chapters.ch12.domain.bandit.trainer.BanditTrainerParameters;
import core.foundation.gadget.timer.CpuTimer;
import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class BanditTrainerDependenciesFactory {


    public static BanditTrainerDependencies produce(EnvironmentParametersBandit envParams,
                                                    BanditTrainerParameters trainerParams) {
        return BanditTrainerDependencies.builder()
                .environment(EnvironmentBanditWrapper.of(envParams))
                .memory(BanditActionValueMemory.create(trainerParams.learningRate()))
                .trainerParameters(trainerParams)
                .timer(CpuTimer.empty())
                .rand(new Random(0))
                .build();
    }
}
