package chapters.ch6.implem.factory;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.factory.EnvironmentParametersSplittingFactory;
import core.gridrl.AgentGridParameters;
import chapters.ch4.implem.treasure.factory.EnvironmentParametersTreasureFactor;
import chapters.ch6.implem.splitting.agent.AgentGridMultiStepBestActionSplitting;
import chapters.ch6.implem.splitting.agent.AgentGridMultiStepLearnerPolicySplitting;
import chapters.ch6.implem.splitting.agent.AgentGridMultiStepRandomActionSplitting;
import chapters.ch6.implem.treasure.agent.AgentGridMultiStepTreasure;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Factory class for creating different types of multi-step agents.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentGridMultiStepFactory {

    public static AgentGridMultiStepBestActionSplitting createSplittingBestAction() {

        var agentParameters =  AgentGridParameters.builder()
                .discountFactor(1.0)
                .tdMax(10)
                .build();
        var gridParameters = EnvironmentParametersSplittingFactory.produce();
        return AgentGridMultiStepBestActionSplitting.of(agentParameters, gridParameters);
    }


    public static AgentGridMultiStepRandomActionSplitting createSplittingRandomAction() {
        var agentParameters =  AgentGridParameters.builder()
                .discountFactor(1.0)
                .tdMax(10)
                .build();
        var gridParameters = EnvironmentParametersSplittingFactory.produce();
        return  AgentGridMultiStepRandomActionSplitting.of(agentParameters,gridParameters);
    }

    public static AgentGridMultiStepLearnerPolicySplitting learnBestActionSplitting(
            EnvironmentParametersSplitting gridPar) {
        var agentParameters =  AgentGridParameters.builder()
                .discountFactor(1.0)
                .tdMax(10)
                .build();
        return AgentGridMultiStepLearnerPolicySplitting.of(agentParameters, gridPar);
    }


    public static AgentGridMultiStepTreasure createTreasure() {
        var agentParameters =  AgentGridParameters.builder()
                .defaultValueStateAction(0)
                .discountFactor(1.0)
                .tdMax(100.0)
                .build();
        var parameters = EnvironmentParametersTreasureFactor.produce();
        return AgentGridMultiStepTreasure.of(agentParameters, parameters);
    }

}
