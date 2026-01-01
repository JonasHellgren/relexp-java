package chapters.ch4.implem.cliff_walk.factory;

import chapters.ch4.domain.agent.AgentQLearningGrid;
import chapters.ch4.domain.agent.AgentSarsaGrid;
import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import chapters.ch4.domain.trainer.core.TrainerOneStepTdQLearning;
import chapters.ch4.domain.trainer.core.TrainerOneStepTdSarsa;
import chapters.ch4.implem.cliff_walk.core.EnvironmentCliff;
import chapters.ch4.implem.cliff_walk.core.EnvironmentParametersCliff;
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
        EnvironmentParametersCliff envParams = FactoryEnvironmentParametersCliff.produceCliff();
        var environment = EnvironmentCliff.of(envParams);
        var agentParamsQL = AgentGridParametersFactoryCliff.sutton();
        var agentParamsSarsa = AgentGridParametersFactoryCliff.sutton();
        var trainerParams = TrainerParametersFactoryCliff.produceManyEpisodes();
        var startStateSupplier = StartStateSupplierCliffXis0RandomY.of(environment);
        var baseDep = TrainerGridDependencies.builder()
                .agent(AgentQLearningGrid.of(agentParamsQL, envParams))
                .environment(environment)
                .trainerParameters(trainerParams)
                .startStateSupplier(startStateSupplier)
                .build();
        return Dependencies.builder()
                .qlearning(baseDep)
                .sarsa(baseDep.withAgent(AgentSarsaGrid.of(agentParamsSarsa, envParams)))
                .build();
    }

}
