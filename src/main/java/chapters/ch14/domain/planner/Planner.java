package chapters.ch14.domain.planner;

import chapters.ch14.domain.action_roller.ActionSequenceRoller;
import chapters.ch14.domain.action_selector.ActionSelectorI;
import chapters.ch14.domain.long_memory.LongMemory;
import chapters.ch14.domain.settings.PlanningSettings;
import core.foundation.util.cond.ConditionalsUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class represents a planner that can generate a sequence of actions to achieve a goal in a given state.
 * It uses an action selector and an action sequence roller to select and roll out actions.
 */
@Log
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Planner<SI, S, A> {
    private static final String FIRST_TRY_FAILED = "All rollouts failed on first try, doing second try with timeMaxMs=";

    record  Predicates <A>(
            Predicate<PlanningStatus<A>> firstTryPredicate,
            Predicate<PlanningStatus<A>> secondTryPredicate
    )
    {
        public static <A> Predicates<A> create(PlanningSettings settings) {
            return new Predicates<>(
                    ps -> ps.nRollouts() >= settings.minNofIterations(),
                    ps -> ps.isTimeExceeded(settings.maxTimeInMs()));
        }
    }

    private final ActionSelectorI<A> actionSelector;
    private final ActionSequenceRoller<SI, S, A> roller;
    private final PlanningSettings settings;
    private final Predicates<A> predicates;

    public static <SI, S, A> Planner<SI, S, A> create(ActionSelectorI<A> actionSelector,
                                                      ActionSequenceRoller<SI, S, A> roller,
                                                      PlanningSettings settings) {
        return new Planner<>(actionSelector, roller, settings, Predicates.create(settings));
    }

    /**
     * Plans a sequence of actions to achieve a goal in a given state.
     * @param stateSupplier A supplier of the initial state.
     * @param memory The long memory to use.
     * @return The planning status after planning.
     */
    public PlanningStatus<A> plan(Supplier<S> stateSupplier, LongMemory<SI> memory) {

        var status = doPlanningTry(stateSupplier, memory, predicates.firstTryPredicate);
        if (status.isAllRolloutsFailed()) {
            log.fine(FIRST_TRY_FAILED + settings.maxTimeInMs());
            status = doPlanningTry(stateSupplier, memory, predicates.secondTryPredicate)
                    .withSecondRolloutTry(true);
        }
        return status;
    }

    @NotNull
    private PlanningStatus<A> doPlanningTry(Supplier<S> stateProvider,
                                            LongMemory<SI> memory,
                                            Predicate<PlanningStatus<A>> predicate) {
        var status = PlanningStatus.<A>empty();
        var bestRollout = BestRollout.<A>empty();
        while (!predicate.test(status)) {
            var actions = actionSelector.selectActions(settings.maxDepth());
            var rollingResult = roller.roll(stateProvider.get(), actions, memory);
            bestRollout = bestRollout.update(rollingResult, actions);
            status = status.update(rollingResult, bestRollout);
        }
        maybeLog(status);
        return status;
    }

    private static <A> void maybeLog(PlanningStatus<A> status) {
        ConditionalsUtil.executeIfTrue(status.nRollouts() == 0, () ->
            log.warning("no rollouts, status=" + status));
    }

}
