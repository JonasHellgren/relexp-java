package chapters.ch13.domain.searcher.core;

import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.tree.Tree;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import static core.foundation.util.cond.ConditionalsUtil.executeIfTrue;


/**
 * This class represents a Monte Carlo Tree Search (MCTS) algorithm.
 * It is responsible for searching the tree and making decisions based on the current state.
 *
 * @param <S> The type of the state.
 * @param <A> The type of the action.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class Searcher<S, A> {

    Dependencies<S, A> dependencies;

    public static <S, A> Searcher<S, A> of(OuterDependencies<S, A> outer) {
        return new Searcher<>(Dependencies.of(outer, SearchWorkers.of(outer)));
    }

    /**
     * Performs the search algorithm on the given root node.
     *
     * @param root The root node of the tree.
     * @return The resulting tree after the search algorithm.
     */
    public Tree<S, A> search(Node<S, A> root) {
        var vars = Variables.of(root);
        for (int i = 0; i < dependencies.maxIterations(); i++) {
            initPath(root, vars);
            traverse(vars);
            var nodeInfo = vars.current.info();
            executeIfTrue(nodeInfo.isExpandable() && isBelowMaxDepth(vars),
                    () -> expand(vars));
            executeIfTrue(nodeInfo.isSimulate(), () -> simulate(vars));
            backPropagate(vars);
        }
        return Tree.of(root);
    }

    public void logTime() {
        log.info("Time (s): " + dependencies.workers().timer().timeInSecondsAsString());
    }

    /**
     * Initializes the path for the current iteration.
     *
     * @param root The root node of the tree.
     * @param vars The current state of the search algorithm.
     */
    private void initPath(Node<S, A> root, Variables<S, A> vars) {
        vars.clearPath();
        vars.addNodeToPath(root);
        vars.setCurrent(root);
    }

    /**
     * Traversal. Starting from the root, follow the tree by choosing child nodes
     */
    private void traverse(Variables<S, A> vars) {
        var selector = dependencies.getSelector();
        while (vars.isNotExpandable() && isBelowMaxDepth(vars)) {
            vars.setCurrent(selector.selectFromTriedActions(vars.current));
            vars.addNodeToPath(vars.current);
        }
    }

    /**
     * Expansion. Add one or more new child nodes (representing new actions).
     */
    private void expand(Variables<S, A> vars) {
        var newChild = dependencies.getExpander().expand(vars.current);
        vars.setCurrent(newChild);
        vars.addNodeToPath(newChild);
    }

    /**
     * Simulation. From the new node, simulate  until terminal state
     */
    private void simulate(Variables<S, A> vars) {
        dependencies.getSimulator().simulate(vars.current, vars.path);
    }

    /**
     * Backpropagation. Use the path to update the stats of all traversed nodes
     */
    private void backPropagate(Variables<S, A> vars) {
        dependencies.getBackpropagator().update(vars.path);
    }

    private boolean isBelowMaxDepth(Variables<S, A> vars) {
        return vars.isTreeBelowMaxDepth(dependencies.maxTreeDepth());
    }


}
