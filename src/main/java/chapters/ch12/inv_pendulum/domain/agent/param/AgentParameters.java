package chapters.ch12.inv_pendulum.domain.agent.param;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record AgentParameters(
        int nHiddenLayers,
        int nInputs,
        int nHiddenUnits,
        int nOutputs,
        double angleMaxMagnitude,   //for net normalization
        double angularSpeedMaxMagnitude,  //for net normalization
        double valueMin,
        double netInMin,
        double netInMax
) {
}
