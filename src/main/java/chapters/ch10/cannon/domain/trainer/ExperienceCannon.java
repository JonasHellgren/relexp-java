package chapters.ch10.cannon.domain.trainer;


import chapters.ch10.cannon.domain.envrionment.StepReturnCannon;

/**
 * Represents a single experience in the cannon environment.
 * An experience consists of an action taken and the resulting step return.
 */

public record ExperienceCannon(double action, StepReturnCannon stepReturn) {

    public static ExperienceCannon of(double action, StepReturnCannon stepReturn) {
        return new ExperienceCannon(action, stepReturn);
    }

    public double reward() {
        return stepReturn.reward();
    }

}
