package chapters.ch13.domain.searcher.expander;

import chapters.ch13.domain.environment.EnvironmentI;
import chapters.ch13.domain.environment.Experience;
import chapters.ch13.domain.searcher.node.Node;
import com.google.common.base.Preconditions;
import core.foundation.util.rand.RandUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.function.Function;


/**
 * An Expander is responsible for expanding a node in the search tree by selecting a new action and creating a new child node.
 *
 * @param <S> the type of state
 * @param <A> the type of action
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Expander<S, A> {

    EnvironmentI<S, A> environment;
    Function<Experience<S, A>, String> nameFunction;

    public static <S, A> Expander<S, A> of(EnvironmentI<S, A> environment,
                                           Function<Experience<S, A>, String> nameFunction) {
        return new Expander<>(environment, nameFunction);
    }

    /**
     * Expands a node in the search tree by selecting a new action and creating a new child node.
     *
     * @param node the node to expand
     * @return the new child node
     */
    public Node<S, A> expand(Node<S, A> node) {
        var action = selectFromNonTriedActions(node);
        S state = node.info().state();
        var exp=Experience.of(state, action, environment.step(state, action));
        var nodeChild = Node.of(
                nameFunction.apply(exp),
                node,
                exp,
                node.info().getActionsAvailable());
        node.addChild(nodeChild);
        return nodeChild;
    }

    private A selectFromNonTriedActions(Node<S, A> node) {
        var actions = node.info().nonTriedActions();
        Preconditions.checkArgument(!actions.isEmpty(),"No actions available");
        int randIdx= RandUtils.getRandomIntNumber(0,actions.size());
        return actions.get(randIdx);
    }

}
