package chapters.ch4.implem.blocked_road_lane.factory;

import chapters.ch4.domain.agent.AgentQLearningGrid;
import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import chapters.ch4.domain.trainer.core.TrainerOneStepTdQLearning;
import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import chapters.ch4.implem.blocked_road_lane.start_state_suppliers.StartStateSupplierRoadMostLeftAnyLane;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoadRunnerFactory {

    @Builder
    public  record Dependencies(
            TrainerGridDependencies qlHighLearning,
            TrainerGridDependencies qlLowLearning,
            TrainerGridDependencies qlHighLearningDiscD9,
            TrainerGridDependencies qlStochasticFailReward
    ) {
    }

    @Builder
    public  record Trainers(
            TrainerOneStepTdQLearning qlHighLearning,
            TrainerOneStepTdQLearning qlLowLearning,
            TrainerOneStepTdQLearning qlHighLearningDiscD9,
            TrainerOneStepTdQLearning qlStochasticFailReward
    )  {
        public void train() {
            qlHighLearning.train();
            qlLowLearning.train();
            qlHighLearningDiscD9.train();
            qlStochasticFailReward.train();
        }

    }

    public static Trainers produceTrainers(Dependencies dependencies) {
        return Trainers.builder()
                .qlHighLearning(TrainerOneStepTdQLearning.of(dependencies.qlHighLearning))
                .qlLowLearning(TrainerOneStepTdQLearning.of(dependencies.qlLowLearning))
                .qlHighLearningDiscD9(TrainerOneStepTdQLearning.of(dependencies.qlHighLearningDiscD9))
                .qlStochasticFailReward(TrainerOneStepTdQLearning.of(dependencies.qlStochasticFailReward()))
                .build();
    }

    public static Dependencies produceDependencies() {
        var envParams = FactoryEnvironmentParametersRoad.produceRoadFixedFailReward();
        var environment = EnvironmentRoad.of(envParams);
        var envParamsStochasticReward = FactoryEnvironmentParametersRoad.produceRoadRandomReward();
        var environmentStochastic = EnvironmentRoad.of(envParamsStochasticReward);

        var agentParamsQL = AgentGridParametersFactoryRoad.produceBase();
        var agentParamsQLDiscD9 = AgentGridParametersFactoryRoad.produceDiscD9();
        var trainerParamsLowLearnHighExp = TrainerParametersFactoryRoad.produceLowLearningHighExploration();
        var trainerParamsHighLearnHighExp = TrainerParametersFactoryRoad.produceHighLearningRateAndExploration();

        var startStateSupplier = StartStateSupplierRoadMostLeftAnyLane.create();
        TrainerGridDependencies baseDep = TrainerGridDependencies.builder()
                .agent(AgentQLearningGrid.of(agentParamsQL, envParams))
                .environment(environment)
                .trainerParameters(trainerParamsHighLearnHighExp)
                .startStateSupplier(startStateSupplier)
                .build();
        return Dependencies.builder()
                .qlHighLearning(baseDep)
                .qlLowLearning(baseDep.withTrainerParameters(trainerParamsLowLearnHighExp))
                .qlHighLearningDiscD9(baseDep.withAgent(AgentQLearningGrid.of(agentParamsQLDiscD9, envParams)))
                .qlStochasticFailReward(baseDep.withEnvironment(environmentStochastic))
                .build();
    }

/*


    public static Dependencies produceDependenciesOld() {
        var envParams = FactoryEnvironmentParametersRoad.produceRoadFixedFailReward();
        var environment = EnvironmentRoad.of(envParams);
        var envParamsStochasticReward = FactoryEnvironmentParametersRoad.produceRoadRandomReward();
        var environmentStochastic = EnvironmentRoad.of(envParamsStochasticReward);
        var agentParamsQL = AgentGridParametersFactoryRoad.produceHighLearningRateAndExploration();
        var agentParamsQLLow = AgentGridParametersFactoryRoad.produceLowLearningHighExploration();
        var agentParamsQLD9 = AgentGridParametersFactoryRoad.produceDiscD9();
        var trainerParams = TrainerParametersFactoryRoad.produceDefault();
        var startStateSupplier = StartStateSupplierRoadMostLeftAnyLane.create();
        TrainerGridDependencies baseDep = TrainerGridDependencies.builder()
                .agent(AgentQLearningGrid.of(agentParamsQL, envParams))
                .environment(environment)
                .trainerParameters(trainerParams)
                .startStateSupplier(startStateSupplier)
                .build();
        return Dependencies.builder()
                .qlHighLearning(baseDep)
                .qlLowLearning(baseDep.withAgent(AgentQLearningGrid.of(agentParamsQLLow, envParams)))
                .qlHighLearningDiscD9(baseDep.withAgent(AgentQLearningGrid.of(agentParamsQLD9, envParams)))
                .qlStochasticFailReward(baseDep.withEnvironment(environmentStochastic))
                .build();
    }
*/


}
