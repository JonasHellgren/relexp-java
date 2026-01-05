package ch10;

import chapters.ch10.bandit._shared.ErrorBandPlotterBandit;
import chapters.ch10.bandit._shared.MeasuresBanditEnum;
import chapters.ch10.bandit.domain.agent.AgentBandit;
import chapters.ch10.bandit.domain.environment.EnvironmentBandit;
import chapters.ch10.bandit.domain.trainer.TrainerBandit;
import chapters.ch10.bandit.domain.trainer.TrainerDependenciesBandit;
import chapters.ch10.bandit.factory.FactoryAgentParametersBandit;
import chapters.ch10.bandit.factory.FactoryEnvironmentParametersBandit;
import chapters.ch10.bandit.factory.FactoryTrainerParameters;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.gadget.timer.CpuTimer;
import lombok.SneakyThrows;
import java.util.List;

public class RunnerTrainerBandit {

    public static final int N_WINDOWS_FILTERING = 10;

    public static final boolean IS_MANY_EPIS = true;

    @SneakyThrows
    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        var agent = AgentBandit.of(FactoryAgentParametersBandit.equalProbability());
        var environmentLeftBetter = EnvironmentBandit.of(
                FactoryEnvironmentParametersBandit.veryHighLeftProbability());
        var trainerPar = (IS_MANY_EPIS)
                ? FactoryTrainerParameters.lowLearningRateManyEpis()
                : FactoryTrainerParameters.highLearningRateFewEpis();
        var dependencies = TrainerDependenciesBandit.of(agent, environmentLeftBetter, trainerPar);
        var trainer = TrainerBandit.of(dependencies);
        trainer.train();
        timer.printInMs();

        var recorder = trainer.getRecorder();
        var path = ProjectPropertiesReader.create().pathPolGrad();
        var plotter1 = ErrorBandPlotterBandit.ofFiltering(
                recorder,
                path,
                "banditReturn_manyEpis_"+IS_MANY_EPIS,
                N_WINDOWS_FILTERING);
        var plotter2 = ErrorBandPlotterBandit.ofNoFiltering(
                recorder,
                path,
                "banditProbGradlog_manyEpis_"+IS_MANY_EPIS);
        plotter1.plotAndSave(List.of(MeasuresBanditEnum.SUMREWARDS));
        plotter2.plotAndSave(List.of(MeasuresBanditEnum.PROBLEFT, MeasuresBanditEnum.GRADLOGLEFT));
    }
}
