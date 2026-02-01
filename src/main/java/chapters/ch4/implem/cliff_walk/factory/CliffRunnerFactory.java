package chapters.ch4.implem.cliff_walk.factory;

import chapters.ch4.domain.agent.AgentQLearningGrid;
import chapters.ch4.domain.agent.AgentSarsaGrid;
import core.gridrl.TrainerGridDependencies;
import chapters.ch4.domain.trainer.TrainerOneStepTdQLearning;
import chapters.ch4.domain.trainer.TrainerOneStepTdSarsa;
import chapters.ch4.implem.cliff_walk.core.EnvironmentCliff;
import chapters.ch4.implem.cliff_walk.core.InformerCliff;
import chapters.ch4.implem.cliff_walk.start_state_suppliers.StartStateSupplierCliffXis0RandomY;
import lombok.Builder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CliffRunnerFactory {

    @Builder
   public record Dependencies(
            TrainerGridDependencies qlearning,
            TrainerGridDependencies sarsa
    ) {
    }

    @Builder
    public record Trainers(
            TrainerOneStepTdQLearning qlearning,
            TrainerOneStepTdSarsa sarsa
    ) {
        public void train() {
            qlearning.train();
            sarsa.train();
        }
    }


    public static Trainers produceTrainers(Dependencies dependencies) {
        return Trainers.builder()
                .qlearning(TrainerOneStepTdQLearning.of(dependencies.qlearning()))
                .sarsa(TrainerOneStepTdSarsa.of(dependencies.sarsa()))
                .build();
    }

    public static Dependencies produceDependencies() {
        var envParams = FactoryEnvironmentParametersCliff.produce();
        var informer= InformerCliff.create(envParams);
        var environment = EnvironmentCliff.of(envParams);
        var agentParamsQL = AgentGridParametersFactoryCliff.sutton();
        var agentParamsSarsa = AgentGridParametersFactoryCliff.sutton();
        var trainerParams = TrainerParametersFactoryCliff.produceManyEpisodes();
        var startStateSupplier = StartStateSupplierCliffXis0RandomY.of(environment);
        var baseDep=TrainerGridDependencies.of(
                AgentQLearningGrid.of(agentParamsQL, informer),
                environment,
                trainerParams,
                startStateSupplier,
                informer);
        return Dependencies.builder()
                .qlearning(baseDep)
                .sarsa(baseDep.withAgent(AgentSarsaGrid.of(agentParamsSarsa, informer)))
                .build();
    }

}
