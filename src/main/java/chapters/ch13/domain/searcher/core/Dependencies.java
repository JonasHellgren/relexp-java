package chapters.ch13.domain.searcher.core;

import chapters.ch13.domain.searcher.workers.BackPropagator;
import chapters.ch13.domain.searcher.workers.Expander;
import chapters.ch13.domain.searcher.workers.Selector;
import chapters.ch13.domain.searcher.workers.Simulator;

public record Dependencies<S, A>(
        OuterDependencies<S, A> outer,
        SearchWorkers<S, A> workers
) {

    public static <S, A> Dependencies<S, A> of(OuterDependencies<S, A> outer, SearchWorkers<S, A> inner) {
        return new Dependencies<>(outer, inner);
    }

    public Expander<S,A> getExpander() {
        return workers().expander();
    }

    public Simulator<S,A> getSimulator() {
        return workers().simulator();
    }

    public BackPropagator<S,A> getBackpropagator() {
        return workers().backpropagator();
    }

    public Selector<S, A>   getSelector() {
        return workers().selector();
    }

    public int maxIterations() {
        return outer.searcherSettings().maxIterations();
    }

    public int maxTreeDepth() {
        return outer.searcherSettings().maxTreeDepth();
    }

    public String timeInSecondsAsString() {
        return workers().timer().timeInSecondsAsString();
    }
}
