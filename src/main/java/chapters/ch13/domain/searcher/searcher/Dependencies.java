package chapters.ch13.domain.searcher.searcher;

import chapters.ch13.domain.environment.EnvironmentI;
import chapters.ch13.domain.environment.Experience;
import chapters.ch13.domain.searcher.settings.SearcherSettings;
import lombok.Builder;
import java.util.function.Function;


/**
 * Represents the classes required for a searcher in the MCTS algorithm.
 */
@Builder
public record Dependencies<S, A>(
        SearcherSettings searcherSettings,
        EnvironmentI<S, A> environment,
        Function<Experience<S, A>, String> nameFunction,
        Function<S, A> rolloutPolicy
) {
}
