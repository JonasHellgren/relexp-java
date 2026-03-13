package ch10;

import chapters.ch10.animation.cannon.AnimationCannon;
import chapters.ch10.cannon.domain.agent.AgentCannon;
import chapters.ch10.cannon.domain.envrionment.EnvironmentCannon;
import chapters.ch10.cannon.domain.trainer.TrainerCannon;
import chapters.ch10.cannon.domain.trainer.TrainerDependenciesCannon;
import chapters.ch10.factory.FactoryAgentParametersCannon;
import chapters.ch10.factory.FactoryEnvironmentParametersCannon;
import chapters.ch10.factory.FactoryTrainerParametersCannon;
import chapters.ch10.plotting.ErrorBandPlotterCannon;
import chapters.ch10.plotting.MeasuresCannonEnum;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierI;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierRandomAndClipped;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.core.TrainerLunarMultiStep;
import chapters.ch11.plotting.AgentEvaluator;
import chapters.ch11.plotting.LunarTrainerPlotter;
import core.foundation.config.ConfigFactory;
import lombok.SneakyThrows;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RunnerTrainerCannonAnimation {

    static final int N_WINDOWS_FILTERING = 10;
    static final String PATH = ConfigFactory.pathPicsConfig().ch11();


    @SneakyThrows
    public static void main(String[] args) {
        var trainer = getTrainer();
        var cfg= ConfigFactory.getAnimationConfig();
        trainer.train(AnimationCannon.create(cfg));
        //plotting(trainer, trainer.getDependencies(),trainer.getDependencies().environment().());
    }

    static TrainerCannon getTrainer() {
        var parEnv = FactoryEnvironmentParametersCannon.createDefault();
        var environment = EnvironmentCannon.of(parEnv);
        var parAgent =  FactoryAgentParametersCannon.animation();
        var agent = AgentCannon.of(parAgent);
        var parTrainer = FactoryTrainerParametersCannon.animation();
        var dependencies = TrainerDependenciesCannon.of(environment, agent, parTrainer);
        return TrainerCannon.of(dependencies);
    }


    private static void plotting(TrainerLunarMultiStep trainer,
                                 TrainerDependencies trainerDependencies,
                                 LunarParameters ep) {
        LunarTrainerPlotter.plot(trainer, trainerDependencies,PATH);
        var evaluator= AgentEvaluator.of(trainerDependencies, getStartStateEvaluation(ep));
    }

    @NotNull
    private static StartStateSupplierI getStartStateEvaluation(LunarParameters ep) {
        return StartStateSupplierRandomAndClipped.create(
                ep, Pair.create(ep.yMax(), ep.yMax()), Pair.create(-ep.spdMax(), -0d));
    }


}
