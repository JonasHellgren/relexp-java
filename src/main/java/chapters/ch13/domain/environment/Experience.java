package chapters.ch13.domain.environment;

import lombok.Builder;

/**
 * Represents a single experience in an environment, consisting of a state, action, reward, and outcome.
 *
 * @param <S> the type of the state
 * @param <A> the type of the action
 */

@Builder
public record Experience<S, A>(
        S state,
        A action,
        double reward,
        S stateNew,
        boolean isTerminal,
        boolean isFail
) {

    public static <S, A> Experience<S, A> of(S state, A action, StepReturnI<S> stepReturn) {
        return new Experience<>(state, action, stepReturn.reward(), stepReturn.stateNew(),
                stepReturn.isTerminal(), stepReturn.isFail());

    }

    public static <S,A> Experience<S,A> noTerm(S state, A action, double reward, S stateNew) {
        return new Experience<>(state, action, reward, stateNew, false, false);
    }

    @Override
    public String toString() {
        return "Experience{" +
                "Action{" + action +
                "}, Reward{" + reward +
                "}, StateNew{" + stateNew +
                "}, isTerminal{" + isTerminal +
                "}, isFail{" + isFail + '}'+
                '}';
    }

}
