package chapters.ch13.domain.searcher.core;

import chapters.ch13.domain.environment.EnvironmentI;
import chapters.ch13.domain.environment.Experience;
import lombok.Builder;
import java.util.function.Function;


/**
 * Represents the classes required for a searcher in the MCTS algorithm.
 */
@Builder
public record Dependencies<S, A>(
        SearcherParameters searcherSettings,
        EnvironmentI<S, A> environment,
        Function<Experience<S, A>, String> nameFunction,
        Function<S, A> rolloutPolicy
) {
}
