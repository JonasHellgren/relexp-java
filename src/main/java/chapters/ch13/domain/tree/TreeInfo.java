package chapters.ch13.domain.tree;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides information about a tree in the Monte Carlo Tree Search (MCTS) algorithm.
 * <p>
 * Example usage:
 * int depth = treeInfo.depth();
 * int numNodes = treeInfo.numberOfNodes();
 * ```
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TreeInfo<S, A> {

    Tree<S, A> tree;

    public static <S, A> TreeInfo<S, A> of(Tree<S, A> tree) {
        return new TreeInfo<>(tree);
    }

    public Node<S, A> root() {
        return tree.getRoot();
    }

    public int depth() {
        return recursiveDepth(root(), 0, 1);
    }

    public int numberOfNodes() {
        return recursiveCountNodes(root(), 1);
    }


    public int numberOfNodesMaxAnyDepth() {
        int nMax = 0;
        for (int i = 0; i < depth(); i++) {
            var nodes = nodesAtDepth(i);
            nMax = Math.max(nMax, nodes.size());
        }
        return nMax;
    }

    public List<Node<S, A>> nodesAtDepth(double depth) {
        List<Node<S, A>> nodes = new ArrayList<>();
        return recursiveNodesAtDepth(root(), depth, 0, nodes);
    }

    public double minValue() {
        return recursiveExtreme(root(), Double.MAX_VALUE, true);
    }

    public double maxValue() {
        return recursiveExtreme(root(), -Double.MAX_VALUE, false);
    }


    private List<Node<S, A>> recursiveNodesAtDepth(Node<S, A> parent,
                                                   double depth,
                                                   int currentDepth,
                                                   List<Node<S, A>> nodes) {
        if (currentDepth == depth) {
            nodes.add(parent);
            return nodes;
        }
        for (var child : parent.info().children()) {
            nodes = recursiveNodesAtDepth(child, depth, currentDepth + 1, nodes);
        }
        return nodes;
    }

    private int recursiveDepth(Node<S, A> node, int maxDepth, int depth) {
        if (node.info().nChildrens() == 0) {
            return depth;
        }
        for (var child : node.info().children()) {
            maxDepth = Math.max(maxDepth, recursiveDepth(child, maxDepth, depth + 1));
        }
        return maxDepth;
    }

    private int recursiveCountNodes(Node<S, A> node, int count) {
        if (node.info().nChildrens() == 0) {
            return count;
        }
        for (var child : node.info().children()) {
            count = recursiveCountNodes(child, count) + 1; // recursively count the children
        }
        return count;
    }

    private double recursiveExtreme(Node<S, A> node, double value, boolean isMin) {
        if (node.info().nChildrens() == 0) {
            return getExtreme(node, value, isMin);
        }
        for (var child : node.info().children()) {
            value = recursiveExtreme(child, getExtreme(node, value, isMin), isMin);
        }
        return value;
    }

    private static <S, A> double getExtreme(Node<S, A> node, double value, boolean isMin) {
        return isMin ? Math.min(value, node.info().value()) : Math.max(value, node.info().value());
    }


}
