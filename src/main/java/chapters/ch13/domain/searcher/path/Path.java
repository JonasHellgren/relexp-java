package chapters.ch13.domain.searcher.path;

import chapters.ch13.domain.environment.Experience;
import chapters.ch13.domain.searcher.node.Node;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a path of visited nodes during one MCTS iteration.
 * A path normaly consists of two phases: tree traversal (selection and expansion)
 * and simulation.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Path<S,A> {

    List<Node<S, A>> nodes;              // Tree traversal phase only (selection + expansion)
    List<Experience<S, A>> simulation;      // Simulation phase (beyond the leaf node)
    PathInfo<S, A> info;

    public static <S, A> Path<S, A> init() {
        return Path.of(new ArrayList<>(), new ArrayList<>());
    }

    public static <S, A> Path<S, A> of(List<Node<S, A>> nodes,
                                       List<Experience<S, A>> simulation) {
        Path<S, A> path= new Path<>(nodes,simulation,null);
        path.info = PathInfo.of(path);
        return  path;
    }

    public void addNode(Node<S, A> node) {
        nodes.add(node);
    }

    public void addExperience(Experience exp) {
        simulation.add(exp);
    }


    public PathInfo<S, A> info() {
        return info;
    }

    public void reset() {
        nodes.clear();
        simulation.clear();
    }

    @Override
    public String toString() {
        var sb=new StringBuilder();
        sb.append(System.lineSeparator());
        sb.append("Nodes={").append(System.lineSeparator());
        for (var node : nodes) {
            sb.append(node.toString()).append(System.lineSeparator());
        }
        sb.append("}").append(System.lineSeparator());
        sb.append("Simulation={").append(System.lineSeparator());
        for (var exp : simulation) {
            sb.append(exp.toString()).append(System.lineSeparator());
        }
        sb.append("}").append(System.lineSeparator());
        return sb.toString();

    }


}
