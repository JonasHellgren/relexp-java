package chapters.ch13.domain.searcher.searcher;

import k_mcts.domain.searcher.node.Node;
import k_mcts.domain.searcher.path.Path;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
class Variables<S, A> {
    Node<S, A> current;
    Path<S, A> path;

    public static <S, A> Variables<S, A> of(Node<S, A> current) {
        return new Variables<>(current,Path.init());
    }

    public void addNodeToPath(Node<S, A> node) {
        path.addNode(node);
    }

    public  boolean isNotExpandable() {
        return !current.info().isTerminal() && current.info().isAllActionsTried();
    }

    public void clearPath() {
        path.reset();
    }

    public boolean isTreeBelowMaxDepth(int maxDepth) {
            return path.info().lengthNodesTree()<maxDepth;
    }

}