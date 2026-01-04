package chapters.ch13.domain.searcher.tree;

import chapters.ch13.domain.searcher.node.Node;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a tree data structure in the context of Monte Carlo Tree Search (MCTS).
 * This class serves as the main entrance for tree operations, providing methods to create and manipulate the tree.
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Tree<S, A> {

    Node<S, A> root;
    TreeInfo<S, A> info;

    public static <S, A> Tree<S, A> of(Node<S, A> root) {
        var tree= new Tree<>(root,null);
        tree.info = TreeInfo.of(tree);
        return tree;
    }

    /**
     * Returns additional information about the tree. For example max depth.
     *
     * @return Additional information about the tree.
     */
    public TreeInfo<S, A> info() {
        return info;
    }


    @Override
    public String toString() {
        var sb=new StringBuilder();
        int depth = 0;
        sb = recursivePrint(root,depth, sb);
        return sb.toString();
    }


    public StringBuilder recursivePrint(Node<S, A> node, int depth, StringBuilder sb) {
        sb.append(" ".repeat(depth)).append(node).append(System.lineSeparator());
        for (var child : node.info().children()) {
            sb = recursivePrint(child,depth+1,sb);
        }
        return sb;
    }


}
