package chapters.ch4.implem.treasure.factory;

import chapters.ch4.domain.agent.AgentQLearningGrid;
import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import chapters.ch4.domain.trainer.core.TrainerOneStepTdQLearning;
import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import chapters.ch4.implem.treasure.start_state_suppliers.StartStateSupplierTreasureMostLeft;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TreasureRunnerFactory {

    @Builder
    public record Dependencies(
            TrainerGridDependencies lowExploration,
            TrainerGridDependencies highExploration
    ) {
    }

    @Builder
    public record Trainers(
            TrainerOneStepTdQLearning lowExploration,
            TrainerOneStepTdQLearning highExploration
    ) {
        public void train() {
            lowExploration.train();
            highExploration.train();
        }
    }

    public static Trainers produceTrainers(Dependencies dependencies) {
        return Trainers.builder()
                .lowExploration(TrainerOneStepTdQLearning.of(dependencies.lowExploration()))
                .highExploration(TrainerOneStepTdQLearning.of(dependencies.highExploration()))
                .build();
    }

    public static Dependencies produceDependencies() {
        var envParams = EnvironmentParametersTreasureFactor.produce();
        var environment = EnvironmentTreasure.of(envParams);
        var agentParam = FactoryAgentGridParametersTreasure.produceBase();
//        var agentParamHighExp = FactoryAgentGridParametersTreasure.produceHighExp();
        var trainerParamsLowExp = FactoryTrainerParametersTreasure.produceManyEpisodesLittleExploration();
        var trainerParamsHighExp = FactoryTrainerParametersTreasure.produceManyEpisodesMuchExploration();
        //var trainerParams = FactoryTrainerParametersTreasure.produceTinyEpisodes();
        var startStateSupplier = StartStateSupplierTreasureMostLeft.create();
        var lowExpDep = TrainerGridDependencies.builder()
                .agent(AgentQLearningGrid.of(agentParam, envParams))
                .environment(environment)
                .trainerParameters(trainerParamsLowExp)
                .startStateSupplier(startStateSupplier)
                .build();
        var highExpDep = TrainerGridDependencies.builder()
                .agent(AgentQLearningGrid.of(agentParam, envParams))
                .environment(environment)
                .trainerParameters(trainerParamsHighExp)
                .startStateSupplier(startStateSupplier)
                .build();
//        var agentHighExp = AgentQLearningGrid.of(agentParamHighExp, envParams);
        return Dependencies.builder()
                .lowExploration(lowExpDep)
                .highExploration(highExpDep)
                .build();
    }


}
