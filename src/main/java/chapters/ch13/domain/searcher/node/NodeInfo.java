package chapters.ch13.domain.searcher.node;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;


/**
 * Represents information about a node in the search tree.
 */
@Getter
public class NodeInfo<S, A> {

    private final Node<S, A> node;
    List<A> actionsAvailable;

    public NodeInfo(Node<S, A> node, List<A> actions) {
        this.node = node;
        this.actionsAvailable = actions;
    }

    public String name() {
        return node.name;
    }

    public A action() {
        return node.experience.action();
    }

    public double reward() {
        return node.experience.reward();
    }

    public boolean isRoot() {
        return node.family.isRoot();
    }

    public S state() {
        return node.experience.stateNew();
    }

    public Node<S,A> parent() {
        return node.family.parent;
    }

    public List<Node<S, A>> children() {
        return node.family.getChildrens();
    }


    public Node<S, A> childForAction(A action) {
        return children().stream()
                .filter(child -> child.experience.action().equals(action))
                .findFirst()
                .orElse(null);
    }

    public int nChildrens() {
        return children().size();
    }

    public double value() {
        return node.statistics.value();
    }

    public int count() {
        return node.statistics.getCount();
    }

    public boolean isChildPresent(A action) {
        return getNodeStream(action).findAny().isPresent();
    }

    public double valueChild(A action) {
        Preconditions.checkArgument(isChildPresent(action),"Child not found, a="+action);
        return getNodeStream(action)
                .mapToDouble(n -> n.info.value()).sum();
    }

    public int countChild(A action) {
        return getNodeStream(action)
                .mapToInt(n -> n.info.count()).sum();
    }

    public boolean isAllActionsTried() {
        return nChildrens() == actionsAvailable.size();
    }

    public boolean isLeaf() {
        return !isAllActionsTried();
    }

    public  List<A> triedActions() {
        return children().stream()
                .map(child -> child.experience.action())
                .toList();
    }

    public  List<A> nonTriedActions() {
        return actionsAvailable.stream()
                .filter(action -> !triedActions().contains(action))
                .toList();
    }


    public boolean isTerminal() {
        return node.experience.isTerminal();
    }

    public boolean isExpandable() {
        return !isTerminal();
    }

    public boolean isSimulate() {
        return !isTerminal();
    }

    public boolean isFail() {
        return node.experience.isFail();
    }

    @NotNull
    private Stream<Node<S, A>> getNodeStream(A action) {
        return node.family.getChildrens().stream()
                .filter(child -> child.experience.action().equals(action));
    }

}
