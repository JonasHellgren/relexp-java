package chapters.ch5.domain.environment;

/**
 * Represents a single experience in a Monte Carlo simulation.
 */
 public record ExperienceMc (
        StateMcI state,
        ActionMcI action,
        StepReturnMc stepReturn
) {

    public static ExperienceMc of(StateMcI state, ActionMcI action, StepReturnMc stepReturn) {
        return new ExperienceMc(state, action, stepReturn);
    }

}
