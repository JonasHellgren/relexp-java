package chapters.ch8.domain.agent.core;

import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.environment.core.StepReturnParking;
import lombok.Builder;

/**
 * Represents an experience in the parking environment, which includes the current state, action taken, reward received,
 * next state, next action, and whether the experience is a terminal state.
 */
@Builder
public record ExperienceParking(
        StateParking state,
        ActionParking action,
        double reward,
        StateParking stateNew,
        ActionParking actionNew,
        double rewardAverage,
        boolean isPark,
        boolean isDeparting,
        boolean isTerminal
) {


    public double deltaReward() {
        return reward() - rewardAverage();
    }

    public static ExperienceParking of(StateParking s,
                                       ActionParking a,
                                       StepReturnParking sr,
                                       ActionParking actionNew,
                                       double rewardAverage) {
        return ExperienceParking.builder()
                .state(s)
                .action(a)
                .reward(sr.reward())
                .stateNew(sr.stateNew())
                .actionNew(actionNew)
                .rewardAverage(rewardAverage)
                .isPark(sr.isPark())
                .isDeparting(sr.isDeparting())
                .isTerminal(sr.isTerminal())
                .build();
    }
}
