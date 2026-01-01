package chapters.ch4.implem.blocked_road_lane.factory;

import chapters.ch4.domain.param.AgentGridParameters;
import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentGridParametersFactoryRoad {

    public static AgentGridParameters produceBase() {
        return AgentGridParameters.builder()
                .environmentName(EnvironmentRoad.NAME)
                .defaultValueStateAction(0.0)
                .discountFactor(1.0)
                .tdMax(Double.MAX_VALUE)  //no clipping if large
                .build();
    }


    public static AgentGridParameters produceDiscD9() {
        return produceBase()
                .withDiscountFactor(0.9);
    }

}
