package chapters.ch10.cannon.domain.envrionment;

import lombok.Builder;

/**
 * Represents the result of a step in the cannon environment.
 * This record contains the reward, distance, and nOccupied of the cannonball.
 */

@Builder
public record StepReturnCannon(
        double reward,
        double distance,  //in meter
        double angle)   //in rad
{
}
