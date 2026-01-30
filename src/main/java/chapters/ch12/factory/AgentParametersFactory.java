package chapters.ch12.factory;

import chapters.ch12.domain.inv_pendulum.agent.param.AgentParameters;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AgentParametersFactory {


    public static AgentParameters createForTest() {
        return  AgentParameters.builder()
                .nInputs(2)
                .nHiddenUnits(64)
                .nOutputs(3)
                .angleMaxMagnitude(1)
                .angularSpeedMaxMagnitude(2)
                .valueMin(-100)
                .netInMin(-1).netInMax(1)
                .build();
    }

    public static AgentParameters createForTrainerTest() {
        return  AgentParameters.builder()
                .nInputs(2)
                .nHiddenUnits(64)
                .nOutputs(3)
                .angleMaxMagnitude(1)
                .angularSpeedMaxMagnitude(8)
                .valueMin(-100)
                .netInMin(-1).netInMax(1)
                .build();
    }

    public static AgentParameters  createForTrainerRunning(int nHiddenLayers, int nHiddenUnits) {
        return createForTrainerTest()
                .withNHiddenLayers(nHiddenLayers).withNHiddenUnits(nHiddenUnits);
    }
}
