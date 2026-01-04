package chapters.ch13.domain.searcher.searcher;

import k_mcts.domain.searcher.node.Node;
import k_mcts.domain.searcher.tree.Tree;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import static org.hellgren.utilities.conditionals.Conditionals.executeIfTrue;

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
    SearchHelper<S, A> helper;

    public static <S, A> Searcher<S, A> of(Dependencies<S, A> dependencies) {
        return new Searcher<>(dependencies, SearchHelper.of(dependencies));
    }

    /**
     * Performs the search algorithm on the given root node.
     *
     * @param root The root node of the tree.
     * @return The resulting tree after the search algorithm.
     */
    public Tree<S, A> search(Node<S, A> root) {
        var vars = Variables.of(root);
        for (int i = 0; i < dependencies.searcherSettings().maxIterations(); i++) {
            initPath(root, vars);
            traverse(vars);
            var info = vars.current.info();
            executeIfTrue(info.isExpandable() && vars.isTreeBelowMaxDepth(maxTreeDepth()),
                    () -> expand(vars));
            executeIfTrue(info.isSimulate(), () -> simulate(vars));
            backPropagate(vars);
        }
        return Tree.of(root);
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
        var selector = helper.selector();
        while (vars.isNotExpandable() && vars.isTreeBelowMaxDepth(maxTreeDepth())) {
            vars.setCurrent(selector.selectFromTriedActions(vars.current));
            vars.addNodeToPath(vars.current);
        }
    }

    private int maxTreeDepth() {
        return dependencies.searcherSettings().maxTreeDepth();
    }

    /**
     * Expansion. Add one or more new child nodes (representing new actions).
     */
    private void expand(Variables<S, A> vars) {
        var expander = helper.expander();
        var newChild = expander.expand(vars.current);
        vars.setCurrent(newChild);
        vars.addNodeToPath(newChild);
    }

    /**
     * Simulation. From the new node, simulate  until terminal state
     */
    private void simulate(Variables<S, A> vars) {
        var simulator = helper.simulator();
        simulator.simulate(vars.current, vars.path);
    }

    /**
     * Backpropagation. Use the path to update the stats of all traversed nodes
     */
    private void backPropagate(Variables<S, A> vars) {
        var backpropagator = helper.backpropagator();
        backpropagator.update(vars.path);
    }

}
