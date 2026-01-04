package chapters.ch11.domain.environment.core;

/**
 * Represents the variables of the lunar lander environment.
 * This record contains the height (y) and speed (spd) of the lander.
 */
public record VariablesLunar(
        double y, double spd
) {
}
