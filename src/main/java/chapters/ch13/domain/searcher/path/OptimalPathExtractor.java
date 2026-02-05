package chapters.ch13.domain.searcher.path;

import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.searcher.core.OuterDependencies;
import chapters.ch13.domain.searcher.workers.Selector;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Extracts the optimal path from a search tree.
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OptimalPathExtractor<S, A> {

    private final OuterDependencies<S,A> dependencies;

    public static <S, A> OptimalPathExtractor<S, A> of(OuterDependencies<S, A> dependencies) {
        return new OptimalPathExtractor<>(dependencies);
    }

    /**
     * Extracts the optimal path from the search tree starting from the given root node.
     *
     * @param root The root node of the search tree.
     * @return The optimal path extracted from the search tree.
     */
    public Path<S, A> extract(Node<S, A> root) {
        var currentNode = root;
        Path<S, A> path = Path.init();
        Selector<S, A> selector = Selector.of(dependencies.searcherSettings());
        while (!currentNode.info().isTerminal() && !currentNode.info().isLeaf()) {
            currentNode = selector.selectGreedyFromTriedActions(currentNode);
            path.addNode(currentNode);
        }
        return path;
    }

}
