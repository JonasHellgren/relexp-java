package chapters.ch13.domain.searcher.node;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a family of nodes in related to a specific node.
 * A family consists of a parent node and a list of child nodes.
 *
 * @param <S> The type of the state.
 * @param <A> The type of the action.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Family<S, A> {
    Node<S,A> parent;
    List<Node<S, A>> childrens;

    public static <S, A> Family<S, A> of(Node<S, A> parent) {
        return new Family<>(parent, new ArrayList<>());
    }

    void addChild(Node<S, A> child) {
        childrens.add(child);
    }

    boolean isRoot() {
        return parent == null;
    }


}
