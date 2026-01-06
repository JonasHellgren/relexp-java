package chapters.ch4.implem.cliff_walk.factory;

import core.gridrl.AgentGridParameters;
import chapters.ch4.implem.cliff_walk.core.EnvironmentCliff;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentGridParametersFactoryCliff {

    public static AgentGridParameters produceBase() {
        return AgentGridParameters.builder()
                .environmentName(EnvironmentCliff.NAME)
                .defaultValueStateAction(0.0)
                .discountFactor(1.0)
                .tdMax(Double.MAX_VALUE)  //no clipping if large
                .build();
    }

    public static AgentGridParameters sutton() {
        return produceBase()
                .withDefaultValueStateAction(10d)  //encourage exploration of non-visited state
                .withTdMax(10);
    }

}
