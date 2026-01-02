package chapters.ch10.bandit.domain.trainer;


import chapters.ch10.bandit.domain.environment.ActionBandit;
import chapters.ch10.bandit.domain.environment.StepReturnBandit;

/**
 * Represents a single experience in a bandit environment.
 * An experience consists of an action taken and the resulting step return.
 */
public record ExperienceBandit(ActionBandit action, StepReturnBandit stepReturn) {

    public static ExperienceBandit of(ActionBandit action, StepReturnBandit stepReturn) {
        return new ExperienceBandit(action, stepReturn);
    }

    public double reward() {
        return stepReturn.reward();
    }

}
