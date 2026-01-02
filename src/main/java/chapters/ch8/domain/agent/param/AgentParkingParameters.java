package chapters.ch8.domain.agent.param;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record AgentParkingParameters(
        double tdMax,
        double defaultValueStateAction,
        double gamma
) {

}
