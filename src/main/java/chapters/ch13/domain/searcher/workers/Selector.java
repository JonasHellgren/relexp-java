package chapters.ch13.domain.searcher.workers;

import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.searcher.core.SearcherParameters;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles selection using UCT (Upper Confidence bound applied to Trees).
 * This class is responsible for selecting the next node to explore in the search tree.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Selector<S, A> {

    private final SearcherParameters searchSettings;

    public static <S, A> Selector<S, A> of(SearcherParameters searchSettings) {
        return new Selector<>(searchSettings);
    }

    /**
     * Selects the next node to explore from the given node's tried actions.
     * This method uses the UCT exploration parameter from the SearcherSettings.
     *
     * @param node the current node
     * @return the selected node
     */
    public Node<S, A> selectFromTriedActions(Node<S, A> node) {
        return selectFromTriedActions(node, searchSettings.uctExploration());
    }

    /**
     * Selects the next node to explore from the given node's tried actions using a greedy strategy.
     * This method sets the UCT exploration parameter to 0, effectively disabling exploration.
     *
     * @param node the current node
     * @return the selected node
     */
    public Node<S, A> selectGreedyFromTriedActions(Node<S, A> node) {
        return selectFromTriedActions(node, 0);
    }

    private Node<S, A> selectFromTriedActions(Node<S, A> node, double uctExploration) {
        var triedActions = node.info().triedActions();
        Preconditions.checkArgument(!triedActions.isEmpty(), "No actions available");
        var nodeValueMap = createNodeDoubleMap(node, uctExploration, triedActions);
        return nodeValueMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();
    }


    private double valueUct(Node<S, A> parent, Node<S, A> child, double uctExploration) {
        double parentCount = parent.info().count();
        double childCount = child.info().count();
        double childValue = child.info().value();
        Preconditions.checkArgument(parentCount > 0, "Parent count cannot be zero");
        return childCount == 0
                ? Double.MAX_VALUE
                : childValue + uctExploration * Math.sqrt(Math.log(parentCount) / childCount);
    }


    @NotNull
    private Map<Node<S, A>, Double> createNodeDoubleMap(Node<S, A> node, double uctExploration, List<A> triedActions) {
        Map<Node<S, A>, Double> nodeValueMap = new HashMap<>();
        for (var action : triedActions) {
            var child = node.info().childForAction(action);
            nodeValueMap.put(child, valueUct(node, child, uctExploration));
        }
        return nodeValueMap;
    }

}
