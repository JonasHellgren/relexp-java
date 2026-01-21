package chapters.ch6.implem.factory;

import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import chapters.ch4.implem.treasure.factory.EnvironmentParametersTreasureFactor;
import chapters.ch4.implem.treasure.start_state_suppliers.StartStateSupplierTreasureMostLeft;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import chapters.ch6.domain.trainer.param.TrainerParametersMultiStepGrid;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

@UtilityClass
public class TrainerDependenciesFactoryTreasure {

    public static final double REWARD_MOVE_SMALL_PEN = -1e-2;
    public static final double LR_END_FRACTION = 0.1;
    public static final double HIGH_PROB = 0.9;
    public static final double MODERATE_PROB = 0.1;

    public static TrainerDependenciesMultiStep treasure(int backupHorizon,
                                                        int nEpisodes,
                                                        double learningRateStart) {
        var agent = AgentGridMultiStepFactory.createTreasure();
        var parametersTreasure = EnvironmentParametersTreasureFactor.produce();
        parametersTreasure=parametersTreasure.withRewardAtFailPos(0d);  //no penalty for sarsa (on-policy learning)
        parametersTreasure=parametersTreasure.withRewardMove(REWARD_MOVE_SMALL_PEN);
        var environment = EnvironmentTreasure.of(parametersTreasure);
        var trainerParameters = TrainerParametersMultiStepGrid.builder()
                .nStepsMax(100)
                .nEpisodes(nEpisodes)
                .gamma(1)
                .probRandomActionStartAndEnd(Pair.create(HIGH_PROB, MODERATE_PROB))
                .learningRateStartAndEnd(Pair.create(learningRateStart, learningRateStart* LR_END_FRACTION))
                .backupHorizon(backupHorizon)
                .build();
        var startStateSupplier = StartStateSupplierTreasureMostLeft.create();
        return TrainerDependenciesMultiStep.of(agent, environment, trainerParameters, startStateSupplier);

    }


}
