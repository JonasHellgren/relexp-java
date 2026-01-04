package chapters.ch14.domain.planner;

import chapters.ch14.domain.action_roller.RollingResult;
import lombok.extern.java.Log;
import java.util.List;

/**
 * This class represents the best rollout so far in during planning.
 * It stores the accumulated rewards and the sequence of actions
 * taken during the rollout.
 */
@Log
public record BestRollout<A>(
        double returnBest,
        List<A> actions

) {

    public static <A> BestRollout<A> empty() {
        return new BestRollout<>(-Double.MAX_VALUE, null);
    }

    public static <A> BestRollout<A> of(double returnBest, List<A> actions) {
        return new BestRollout<>(returnBest, actions);
    }

    /**
     * Updates the BestRollout with the accumulated reward and actions of a new RollingResult.
     * If the new reward is better than the current best reward, a new BestRollout is created with the new reward and actions.
     * Otherwise, the current BestRollout is returned.
     *
     * @param rollingResult The RollingResult containing the accumulated reward and actions
     * @param actions The actions taken during the rollingResult
     * @return A new BestRollout with the better reward and actions, or the current BestRollout
     */
    public BestRollout<A> update(RollingResult rollingResult, List<A> actions) {
        return rollingResult.accRewards()>returnBest
                ? BestRollout.of(rollingResult.accRewards(), actions)
                : this;
    }
}