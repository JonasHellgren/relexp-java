package chapters.ch7.factory;

import chapters.ch4.domain.agent.AgentQLearningGrid;
import chapters.ch4.domain.param.AgentGridParameters;
import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import chapters.ch4.domain.trainer.param.TrainerGridParameters;
import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import chapters.ch4.implem.treasure.factory.EnvironmentParametersTreasureFactor;
import chapters.ch4.implem.treasure.start_state_suppliers.StartStateSupplierTreasureMostLeft;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;


/**
 * A utility class for creating trainer dependencies for the treasure hunt environment.
 */
@UtilityClass
public class TrainerDependencySafeFactory {

    public static final double K_PROB_END = 0.1;
    public static final double K_LEARNING_RATE_END = 0.1;


    public static TrainerGridDependencies treasureForTest() {
        return treasure(10, 0.9, 0.1);
    }

    public static TrainerGridDependencies treasure(int nEpisodes, double learningRateStart, double probRandStart) {
        var envParams = EnvironmentParametersTreasureFactor.produce();
        var environment = EnvironmentTreasure.of(envParams);
        var startStateSupplier = StartStateSupplierTreasureMostLeft.create();
        return TrainerGridDependencies.builder()
                .agent(AgentQLearningGrid.of(agentGridParameters(), envParams))
                .environment(environment)
                .trainerParameters(trainerGridParameters(nEpisodes, learningRateStart,probRandStart))
                .startStateSupplier(startStateSupplier)
                .build();
    }

    private static AgentGridParameters agentGridParameters() {
        return AgentGridParameters.builder()
                .environmentName(EnvironmentTreasure.NAME)
                .defaultValueStateAction(0)
                .discountFactor(1.0)
                .tdMax(Double.MAX_VALUE)
                .build();
    }

    private static TrainerGridParameters trainerGridParameters(int nEpisodes, double learningRateStart, double probRandStart) {
        return TrainerGridParameters.newDefault()
                .withEnvironmentName(EnvironmentTreasure.NAME)
                .withPenaltyActionCorrection(-1)
                .withNStepsMax(100)
                .withLearningRateStartAndEnd(Pair.create(learningRateStart, learningRateStart* K_LEARNING_RATE_END))
                .withProbRandomActionStartAndEnd(Pair.create(probRandStart, probRandStart* K_PROB_END))
                .withNEpisodes(nEpisodes);
    }

}
