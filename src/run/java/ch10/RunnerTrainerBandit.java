package ch10;

import chapters.ch10.plotting.ErrorBandPlotterBandit;
import chapters.ch10.plotting.MeasuresBanditEnum;
import chapters.ch10.bandit.domain.agent.AgentBandit;
import chapters.ch10.bandit.domain.environment.EnvironmentBandit;
import chapters.ch10.bandit.domain.trainer.TrainerBandit;
import chapters.ch10.bandit.domain.trainer.TrainerDependenciesBandit;
import chapters.ch10.factory.FactoryAgentParametersBandit;
import chapters.ch10.factory.FactoryEnvironmentParametersBandit;
import chapters.ch10.factory.FactoryTrainerParameters;
import core.foundation.config.ConfigFactory;
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
        System.out.println("trainer.getRecorder().size() = " + trainer.getRecorder().size());
        timer.printInMs();

        var recorder = trainer.getRecorder();
        var path= ConfigFactory.pathPicsConfig().ch10();
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
