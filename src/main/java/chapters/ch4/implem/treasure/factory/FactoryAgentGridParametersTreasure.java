package chapters.ch4.implem.treasure.factory;

import core.gridrl.AgentGridParameters;
import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FactoryAgentGridParametersTreasure {

    public static AgentGridParameters produceBase() {
        return AgentGridParameters.builder()
                .environmentName(EnvironmentTreasure.NAME)
                .defaultValueStateAction(0)
                .discountFactor(1.0)
                .tdMax(Double.MAX_VALUE)
                .build();
    }

    public static AgentGridParameters produceLowExpHighDefaultValue() {
        return produceBase()
                .withDefaultValueStateAction(10);
    }

}
