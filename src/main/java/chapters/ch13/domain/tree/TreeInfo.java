package chapters.ch13.domain.tree;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.arrow.flatbuf.Int;

import java.util.ArrayList;
import java.util.List;

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


    //TODO FIXA
    public List<Integer> numberOfNodesEachDepth() {
        List<Integer> countList=new ArrayList<>();
        countList.add(1);
        return countNodesAtEachDepth(root(),countList);
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


    private List<Integer> countNodesAtEachDepth(Node<S, A> node,List<Integer> countList) {
        if (node.info().nChildrens()==0) {
            return countList;
        }
        for (var child : node.info().children()) {
            countList.add(node.info().nChildrens());
            countList = countNodesAtEachDepth(child,countList); // recursively count the children
        }
        return countList;
    }


}
