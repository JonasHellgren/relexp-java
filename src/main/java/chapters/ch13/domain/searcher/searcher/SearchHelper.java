package chapters.ch13.domain.searcher.searcher;

import k_mcts.domain.searcher.backpropagator.BackPropagator;
import k_mcts.domain.searcher.expander.Expander;
import k_mcts.domain.searcher.selector.Selector;
import k_mcts.domain.searcher.simulator.Simulator;

/**
 * Represents a helper class for the MCTS algorithm, encapsulating the key components:
 * selector, simulator, backpropagator, and expander.
 */
public record SearchHelper<S, A>(
        Selector<S, A> selector,
        Simulator<S, A> simulator,
        BackPropagator<S, A> backpropagator,
        Expander<S, A> expander
) {
    public static <S, A> SearchHelper<S, A> of(Dependencies<S, A> dependencies) {
        return new SearchHelper<>(
                Selector.of(dependencies.searcherSettings()),
                Simulator.of(dependencies.environment(),
                        dependencies.rolloutPolicy(),
                        dependencies.searcherSettings()),
                BackPropagator.of(dependencies.searcherSettings()),
                Expander.of(dependencies.environment(),
                        dependencies.nameFunction())
        );
    }
}
