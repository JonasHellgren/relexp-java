package chapters.ch6.implem.factory;

import chapters.ch3.factory.EnvironmentParametersSplittingFactory;
import chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import chapters.ch6.domain.trainer.param.TrainerParametersMultiStepGrid;
import chapters.ch6.implem.splitting.agent.StartStateGridSupplierMostLeftSplitting;
import core.gridrl.ActionGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;
import java.util.List;

/**
 * Factory class for creating TrainerDependenciesMultiStep instances.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TrainerDependenciesFactorySplitting {

    public static final double REWARD_MOVE_SMALL_PEN = -1e-2;
    public static final double LR_END_FRACTION = 0.1;
    public static final double SMALL_PROB = 0.01;
    public static final double HIGH_PROB = 0.9;
    public static final int N_STEPS_MAX = 100;

    public static TrainerDependenciesMultiStep givenOptimalPolicySplitting(int backupHorizon,
                                                                           int nEpisodes,
                                                                           double learningRateStart) {
        var agent = AgentGridMultiStepFactory.createSplittingBestAction();
        var gridParameters = EnvironmentParametersSplittingFactory.produce();
        var environment = EnvironmentSplittingPath.of(gridParameters);
        var trainerParameters = getTrainerParameters(backupHorizon, nEpisodes, learningRateStart);
        var startStateSupplier = StartStateGridSupplierMostLeftSplitting.create();
        return TrainerDependenciesMultiStep.of(agent, environment, trainerParameters, startStateSupplier);
    }

    public static TrainerDependenciesMultiStep givenRandomPolicySplitting(int backupHorizon,
                                                                          int nEpisodes,
                                                                          double learningRateStart) {
        var agent = AgentGridMultiStepFactory.createSplittingRandomAction();
        var gridParameters = EnvironmentParametersSplittingFactory.produce();
        var environment = EnvironmentSplittingPath.of(gridParameters);
        var trainerParameters = getTrainerParameters(backupHorizon, nEpisodes, learningRateStart);
        var startStateSupplier = StartStateGridSupplierMostLeftSplitting.create();
        return TrainerDependenciesMultiStep.of(agent, environment, trainerParameters, startStateSupplier);
    }


    public static TrainerDependenciesMultiStep learnPolicySplittingAfterEpis(int backupHorizon,
                                                                             int nEpisodes,
                                                                             double learningRateStart) {
        var gridParameters = EnvironmentParametersSplittingFactory.produce()
                .withValidActions(List.of(ActionGrid.N, ActionGrid.S, ActionGrid.E))
                .withRewardMove(REWARD_MOVE_SMALL_PEN);
        var agent = AgentGridMultiStepFactory.learnBestActionSplitting(gridParameters);
        var environment = EnvironmentSplittingPath.of(gridParameters);
        var trainerParameters = getTrainerParameters(backupHorizon, nEpisodes, learningRateStart);
        var startStateSupplier = StartStateGridSupplierMostLeftSplitting.create();
        return TrainerDependenciesMultiStep.of(agent, environment, trainerParameters, startStateSupplier);
    }


    private static TrainerParametersMultiStepGrid getTrainerParameters(int backupHorizon, int nEpisodes, double learningRateStart) {
        return TrainerParametersMultiStepGrid.builder()
                .nStepsMax(N_STEPS_MAX)
                .nEpisodes(nEpisodes)
                .gamma(1)
                .learningRateStartAndEnd(Pair.create(learningRateStart, learningRateStart *LR_END_FRACTION))
                .probRandomActionStartAndEnd(Pair.create(HIGH_PROB, SMALL_PROB))
                .backupHorizon(backupHorizon)
                .build();
    }

    public static TrainerDependenciesMultiStep learnPolicySplittingDuringEpis(int nStepsHorizon,
                                                                              int nEpisodes,
                                                                              double learningRateStart) {
        var dependencies = learnPolicySplittingAfterEpis(nStepsHorizon, nEpisodes, learningRateStart);
        var trainerParameters = dependencies.trainerParameters();
        var gridParameters = EnvironmentParametersSplittingFactory.produce()
                .withValidActions(List.of(ActionGrid.N, ActionGrid.S, ActionGrid.E))
                .withRewardMove(REWARD_MOVE_SMALL_PEN);
        return dependencies.withTrainerParameters(trainerParameters
                .withProbRandomActionStartAndEnd(Pair.create(HIGH_PROB, SMALL_PROB)))
                .withEnvironment(EnvironmentSplittingPath.of(gridParameters));
    }


}
