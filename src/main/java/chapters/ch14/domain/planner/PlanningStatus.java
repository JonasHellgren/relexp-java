package chapters.ch14.domain.planner;

import chapters.ch14.domain.action_roller.RollingResult;
import core.foundation.gadget.timer.CpuTimer;
import lombok.Builder;
import lombok.With;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This record represents the status of a planning process.
 * It contains information about the planning process such as the time taken, the number of rollouts,
 * the number of failed rollouts, the best return, the actions taken, and whether it's a second rollout try.
 */
@Builder
@With
public record PlanningStatus<A>(
        CpuTimer timer,
        int nRollouts,
        int nRolloutsFailed,
        double returnBest,
        List<A> actions,
        boolean isSecondRolloutTry
) {

    public static <A> PlanningStatus<A>  empty() {
        return PlanningStatus.<A>builder()
                .timer(CpuTimer.empty())
                .nRollouts(0)
                .nRolloutsFailed(0)
                .returnBest(0)
                .actions(new ArrayList<>())
                .isSecondRolloutTry(false)
                .build();
    }

    public PlanningStatus <A> update(RollingResult rollingResult, BestRollout<A> bestRollout) {

        return PlanningStatus.<A>builder()
                .timer(timer)
                .nRollouts(nRollouts + 1)
                .nRolloutsFailed(nRolloutsFailed + (rollingResult.isEndFail() ? 1 : 0))
                .returnBest(bestRollout.returnBest())
                .actions(bestRollout.actions())
                .isSecondRolloutTry(false)
                .build();
    }

    public Optional<A> firstAction() {
        return actions.isEmpty()
                ? Optional.empty() : Optional.of(actions.get(0));
    }


    public boolean isTimeExceeded(long timeMaxInMs) {
        return timer.absoluteProgressInMillis() > timeMaxInMs;
    }

    public int nRolloutsSucceeded() {
        return nRollouts - nRolloutsFailed;
    }

    public boolean isAllRolloutsFailed() {
        return nRolloutsSucceeded() == 0;
    }

    public boolean isSomeRolloutsSuccess() {
        return nRolloutsSucceeded() > 0;
    }

}
