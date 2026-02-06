package ch10;

import chapters.ch10.cannon.domain.agent.AgentCannon;
import chapters.ch10.cannon.domain.envrionment.EnvironmentCannon;
import chapters.ch10.cannon.domain.trainer.TrainerCannon;
import chapters.ch10.cannon.domain.trainer.TrainerDependenciesCannon;
import chapters.ch10.factory.FactoryAgentParametersCannon;
import chapters.ch10.factory.FactoryEnvironmentParametersCannon;
import chapters.ch10.factory.FactoryTrainerParametersCannon;
import chapters.ch10.plotting.ErrorBandPlotterCannon;
import chapters.ch10.plotting.MeasuresCannonEnum;
import core.foundation.config.ConfigFactory;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.gadget.timer.CpuTimer;
import lombok.SneakyThrows;
import java.io.IOException;
import java.util.List;

public class RunnerTrainerCannon {

    static final int N_WINDOWS_FILTERING = 10;
    static final boolean CLIP = true;

    @SneakyThrows
    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        var trainer = getTrainer();
        trainer.train();
        timer.printInMs();
        plotting(trainer);
    }


    static TrainerCannon getTrainer() {
        var parEnv = FactoryEnvironmentParametersCannon.createDefault();
        var environment = EnvironmentCannon.of(parEnv);
        var parAgent = (CLIP)
                ? FactoryAgentParametersCannon.forRunning()
                : FactoryAgentParametersCannon.forRunningNoMeanClipping();
        var agent = AgentCannon.of(parAgent);
        var parTrainer = FactoryTrainerParametersCannon.forRunning();
        var dependencies = TrainerDependenciesCannon.of(environment, agent, parTrainer);
        return TrainerCannon.of(dependencies);
    }

    static void plotting(TrainerCannon trainer) throws IOException {
        var recorder = trainer.getRecorder();
        var path= ConfigFactory.pathPicsConfig().ch10();
        var plotter = ErrorBandPlotterCannon.ofFiltering(
                recorder,
                path,
                "cannon_clipped=" + CLIP,
                N_WINDOWS_FILTERING);
        plotter.plotAndSave(List.of(
                MeasuresCannonEnum.RETURN_MINUS_BASE,
                MeasuresCannonEnum.BASE,
                MeasuresCannonEnum.ANGLE,
                MeasuresCannonEnum.DISTANCE,
                MeasuresCannonEnum.MEAN,
                MeasuresCannonEnum.STD));
    }

}
