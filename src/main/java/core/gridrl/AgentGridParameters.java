package core.gridrl;

import lombok.Builder;
import lombok.With;

/**
 * Represents the parameters for an agent in a grid environment.
 */
@Builder
@With
public record AgentGridParameters(
        String environmentName,         // Environment name
        double defaultValueStateAction, // Default state-action value
        double discountFactor,          // Discount factor for expected return
        double tdMax                    // TD error clipping threshold, no clipping if large
) {

}
