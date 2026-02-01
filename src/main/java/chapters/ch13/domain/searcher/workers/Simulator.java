package chapters.ch13.domain.searcher.workers;

import chapters.ch13.domain.environment.EnvironmentI;
import chapters.ch13.domain.environment.Experience;
import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.searcher.path.Path;
import chapters.ch13.domain.searcher.core.SearcherParameters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import java.util.function.Function;

/**
 * Simulator class responsible for simulating the rollout of a given node in the search tree.
 *
 * @param <S> State type
 * @param <A> Action type
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class Simulator<S, A> {

    public static final String NAME_START = "d";
    EnvironmentI<S, A> environment;
    Function<S, A> rolloutPolicy;
    SearcherParameters searcherSettings;

    public static <S, A> Simulator<S, A> of(EnvironmentI<S, A> environment,
                                            Function<S, A> rolloutPolicy,
                                            SearcherParameters searcherSettings) {
        return new Simulator<>(environment, rolloutPolicy, searcherSettings);
    }

    /**
     * Simulates the rollout of a given node in the search tree.
     *
     * @param nodeStart Starting node
     * @param path      Path to simulate
     * @return Simulated path
     * <p>
     * Ideal to copy path initially, now mutates path
     */
    public Path<S, A> simulate(Node<S, A> nodeStart, Path<S, A> path) {

        var node = nodeStart;
        var depth = path.info().lengthNodesTree();
        while (isContinueSimulation(node, depth)) {
            var exp = getExperience(node);
            path.addExperience(exp);
            node = getNodeNew(depth, node, exp);
            depth++;
        }
        return path;
    }

    private boolean isContinueSimulation(Node<S, A> node, int depth) {
        return !node.info().isTerminal() && depth < searcherSettings.maxDepth();
    }

    private static <S, A> Node<S, A> getNodeNew(int depth,
                                                Node<S, A> node,
                                                Experience<S, A> exp) {
        return Node.of(NAME_START + depth,
                node,
                exp,
                node.info().getActionsAvailable());
    }

    private Experience<S, A> getExperience(Node<S, A> node) {
        S state = node.info().state();
        var action = rolloutPolicy.apply(state);
        var stepReturn = environment.step(state, action);
        return Experience.of(state, action, stepReturn);
    }

}
