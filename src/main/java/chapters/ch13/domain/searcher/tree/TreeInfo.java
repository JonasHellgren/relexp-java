package chapters.ch13.domain.searcher.tree;

import k_mcts.domain.searcher.node.Node;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Provides information about a tree in the Monte Carlo Tree Search (MCTS) algorithm.
 *
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
        return recursiveDepth(root(),0,1);
    }

    public int numberOfNodes() {
        return countNodes(root(),1);
    }

    private int recursiveDepth(Node<S, A> node,int maxDepth,int depth) {
        if (node.info().nChildrens()==0) {
            return depth;
        }
        for (var child : node.info().children()) {
            maxDepth = Math.max(maxDepth, recursiveDepth(child,maxDepth,depth+1));
        }
        return maxDepth;
    }

    private int countNodes(Node<S, A> node,int count) {
        if (node.info().nChildrens()==0) {
            return count;
        }
        for (var child : node.info().children()) {
            count = countNodes(child,count)+1; // recursively count the children
        }
        return count;
    }

}
