package chapters.ch13.domain.searcher.searcher;

import k_mcts.domain.environment.EnvironmentI;
import k_mcts.domain.environment.Experience;
import k_mcts.domain.searcher.settings.SearcherSettings;
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
