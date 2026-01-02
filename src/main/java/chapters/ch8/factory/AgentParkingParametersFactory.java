package chapters.ch8.factory;

import chapters.ch8.domain.agent.param.AgentParkingParameters;
import lombok.experimental.UtilityClass;


/**
 * A utility class for creating AgentParkingParameters instances.
 */
@UtilityClass
public class AgentParkingParametersFactory {

    public static AgentParkingParameters forTest() {
        return AgentParkingParameters.builder()
                .tdMax(1)
                .defaultValueStateAction(0.0)
                .gamma(0.99)
                .build();

    }

    public static AgentParkingParameters forRunning() {
        return forTest().withTdMax(100.0).withGamma(1.0);
    }


}
