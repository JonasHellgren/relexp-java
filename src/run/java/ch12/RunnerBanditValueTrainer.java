package ch12;

import chapters.ch10.bandit.domain.environment.EnvironmentParametersBandit;
import chapters.ch10.factory.FactoryEnvironmentParametersBandit;
import chapters.ch12.domain.bandit.environment.EnvironmentBanditWrapper;
import chapters.ch12.domain.bandit.trainer.BanditActionValueMemory;
import chapters.ch12.domain.bandit.trainer.BanditActionValueTrainer;
import chapters.ch12.domain.bandit.trainer.BanditTrainerDependencies;
import chapters.ch12.domain.bandit.trainer.BanditTrainerParameters;
import chapters.ch12.factory.BanditTrainerDependenciesFactory;
import chapters.ch12.factory.BanditTrainerParametersFactory;
import chapters.ch12.plotting_bandit.ErrorBandPlotterNeuralBandit;
import chapters.ch12.plotting_bandit.MeasuresBanditNeuralEnum;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.gadget.timer.CpuTimer;
import lombok.SneakyThrows;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class RunnerBanditValueTrainer {

    static final int N_WINDOWS_FILTERING = 10;

    @SneakyThrows
    public static void main(String[] args) {
        var envParams= FactoryEnvironmentParametersBandit.veryHighLeftProbability();
        var trainerParams= BanditTrainerParametersFactory.produce();
        var deps = BanditTrainerDependenciesFactory.produce(envParams, trainerParams);
        var trainer= BanditActionValueTrainer.create(deps);
        trainer.train();
        trainer.logTime();
        plotting(trainer);
    }


    static void plotting(BanditActionValueTrainer trainer) throws IOException {
        var recorder = trainer.getRecorder();
        var path = ProjectPropertiesReader.create().pathDeepRl();
        var plotter = ErrorBandPlotterNeuralBandit.ofFiltering(
                recorder,
                path,
                "neural_bandit",
                N_WINDOWS_FILTERING);
        plotter.plotAndSave(List.of(
                MeasuresBanditNeuralEnum.LOSS,
                MeasuresBanditNeuralEnum.VALUE_LEFT,
                MeasuresBanditNeuralEnum.VALUE_RIGHT));
    }


}
