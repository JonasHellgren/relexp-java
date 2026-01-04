package chapters.ch13.domain.searcher.node;

import k_mcts.domain.environment.Experience;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Represents a single decision point in the search tree.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Node<S, A> {

    String name;
    Experience<S, A> experience;
    Statistics statistics;
    Family<S, A> family;
    NodeInfo<S, A> info;

    public static <S, A> Node<S, A> of(String name,
                                       Node<S, A> nodeParent,
                                       Experience<S, A> exp,
                                       List<A> actions) {
        var node = new Node<>(name, exp, Statistics.initial(), Family.of(nodeParent), null);
        node.info = new NodeInfo<>(node, actions);
        return node;
    }

    /**
     * Updates the statistics of this node with the given learning rate and sum of returns.
     *
     * @param learningRate The learning rate.
     * @param sumReturns The sum of returns.
     */
    public void update(double learningRate,double sumReturns) {
        statistics.update(learningRate,sumReturns);
    }


    /**
     * Adds a child node to this node.
     *
     * @param child The child node to add.
     */
    public void addChild(Node<S, A> child) {
        family.addChild(child);
    }


    /**
     * Returns the additional information about this node.
     *
     * @return The node info.
     */
    public NodeInfo<S, A> info() {
        return info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<S, A> that = (Node<S, A>) o;
        return name.equals(that.name) && experience.equals(that.experience);
    }

    @Override
    public int hashCode() {
        return experience.hashCode();
    }

    @Override
    public String toString() {
        return name+", Action{"+info.action()+"}, State{"+info.state()+"}, "+ ", Reward{"+info.reward()+"}, "+
                "isTerm{"+info.isTerminal()+"}, "+"isFail{"+info.isFail()+"}, "+statistics;
    }

}
