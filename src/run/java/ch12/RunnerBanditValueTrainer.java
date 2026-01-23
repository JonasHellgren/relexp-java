package ch12;

import chapters.ch10.factory.FactoryEnvironmentParametersBandit;
import chapters.ch12.bandit.core.BanditActionValueTrainer;
import chapters.ch12.bandit.plotting.ErrorBandPlotterNeuralBandit;
import chapters.ch12.bandit.plotting.MeasuresBanditNeuralEnum;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.gadget.timer.CpuTimer;
import lombok.SneakyThrows;
import java.io.IOException;
import java.util.List;

public class RunnerBanditValueTrainer {

    static final int N_WINDOWS_FILTERING = 10;

    @SneakyThrows
    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        var envParams= FactoryEnvironmentParametersBandit.veryHighLeftProbability();
        var trainer= BanditActionValueTrainer.create(envParams);
        trainer.train();
        timer.printInMs();
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
