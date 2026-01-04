package chapters.ch13.domain.searcher.path;

import com.google.common.base.Preconditions;
import k_mcts.domain.environment.Experience;
import k_mcts.domain.searcher.node.Node;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.hellgren.utilities.list_arrays.MyListUtils;
import org.hellgren.utilities.reinforcement_learning.MyRewardListUtils;

import java.util.List;

/**
 * Represents information about a path in the search tree.
 * For example, the traversed nodes and the total lenght.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PathInfo<S, A> {

    public static final String NEXT_LINE = System.lineSeparator();
    Path<S, A> path;

    public static <S, A> PathInfo<S, A> of(Path<S, A> path) {
        return new PathInfo<>(path);
    }


    public List<Node<S, A>> nodes() {
        return path.nodes;
    }

    public int lengthNodesTree() {
        return path.nodes.size();
    }

    public List<Experience<S, A>> experiencesFromSimulation() {
        return path.simulation;
    }

    public int lengthNodesSimulation() {
        return path.simulation.size();
    }

    public int length() {
        return lengthNodesTree() + lengthNodesSimulation();
    }

    public List<Double> valuesTree() {
        return nodes().stream().map(n -> n.info().value()).toList();
    }

    public boolean isAnyFail() {
        return isAnyFailNodes() || isAnyFailExperiences();
    }

    public boolean isAnyFailNodes() {
        return nodes().stream().anyMatch(n -> n.info().isFail());
    }

    public boolean isAnyFailExperiences() {
        return experiencesFromSimulation().stream().anyMatch(Experience::isFail);
    }

    public List<Double> rewardsTree() {
        return nodes().stream()
                .map(n -> n.info().reward())
                .toList();
    }

    public List<Double> rewardsSimulation() {
        return experiencesFromSimulation().stream().map(Experience::reward).toList();
    }

    public List<Double> rewardsTreeAndSimulation() {
        return MyListUtils.merge(rewardsTree(), rewardsSimulation());
    }

    public int lengthRewardsTreeAndSimulation() {
        return rewardsTreeAndSimulation().size();
    }

    public List<Double> returns(double discount) {
        var rewardsDiscounted = MyRewardListUtils.discountedElements(rewardsTreeAndSimulation(), discount);
        return MyRewardListUtils.getReturns(rewardsDiscounted);
    }

    public boolean isAnyTerminalInTree() {
        return nodes().stream().anyMatch(n -> n.info().isTerminal());
    }

    public boolean isAnyTerminalInSimulation() {
        return experiencesFromSimulation().stream().anyMatch(Experience::isTerminal);
    }

    public double returnForPosInPath(int pos, double discount) {
        Preconditions.checkArgument(pos >= 0, "pos out of bounds");
        Preconditions.checkArgument(pos < lengthNodesTree(), "pos out of bounds");
        var rewardsFromPos = rewardsTreeAndSimulation().subList(pos, lengthRewardsTreeAndSimulation());
        var rewardsDiscounted = MyRewardListUtils.discountedElements(rewardsFromPos, discount);
        return MyListUtils.sumList(rewardsDiscounted);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("PathInfo{");
        sb.append("lengthNodesTree=").append(lengthNodesTree()).append(NEXT_LINE);
        sb.append("lengthNodesSimulation=").append(lengthNodesSimulation()).append(NEXT_LINE);
        sb.append("rewardsTree=").append(rewardsTree()).append(NEXT_LINE);
        sb.append("rewardsSimulation=").append(rewardsSimulation()).append(NEXT_LINE);
        sb.append("returns (non discounted)=").append(returns(1)).append(NEXT_LINE);
        sb.append('}');
        return sb.toString();
    }


}
