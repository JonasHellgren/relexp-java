package chapters.ch13.plotting;

import k_mcts.domain.searcher.node.Node;
import k_mcts.domain.searcher.tree.Tree;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class DotFileGenerator<S, A> {

    public static final String PENWIDH_THICK = "3";
    public static final String PENWIDTH_THIN = "1";
    public static final String LB = "%n";  //line brake
    public static final String COUNT_VALUE_FORMAT = "<%d|%.1f>";
    public static final String NAME_ACTION_FORMAT = "(%s|%s)";
    public static final String NAME_FORMAT = "(%s)";

    private final StringBuilder dot;
    private final List<Node<S, A>> visited;
    private final int maxDepth;

    public static DotFileGenerator init() {
        return init(Integer.MAX_VALUE);
    }


    public static DotFileGenerator init(int maxDepth) {
        return new DotFileGenerator(new StringBuilder(), new ArrayList<>(), maxDepth);
    }

    public String generateDot(Tree<S, A> tree) {
        return generateDot(tree, null);
    }

    public String generateDot(Tree<S, A> tree, List<Node<S, A>> bestPath) {
        dot.setLength(0); // reset
        dot.append("digraph MCTS {\n");
        dot.append("node [shape=ellipse];\n");
        int depth = 0;
        traverse(tree.info().root(), bestPath, depth);
        dot.append("}\n");
        return dot.toString();
    }

    public void writeToFile(String filePath, String dotText) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(dotText);
        }
    }


    private void traverse(Node<S, A> node, List<Node<S, A>> bestPath, int depth) {
        if (visited.contains(node)) {
            log.warning("Node already visited: " + node.info().name());
            return;
        }

        if (depth > maxDepth) {
            log.info("Max depth reached: " + depth);
            return;
        }

        visited.add(node);
        dot.append(textInsideNode(node));
        for (Node<S, A> child : node.info().children()) {
            if (depth < maxDepth) {
                dot.append(textForNodeRelation(node, child, bestPath));
                traverse(child, bestPath, depth + 1);
            }
        }
    }

    @NotNull
    private String textInsideNode(Node<S, A> node) {
        return String.format("N%s [label=\"%s\"];" + LB,
                node.info().name(),
                getNodeLabel(node));
    }

    @NotNull
    private static <S, A> String textForNodeRelation(Node<S, A> node,
                                                     Node<S, A> child,
                                                     List<Node<S, A>> bestPath) {
        var edgeStyle = Optional.ofNullable(bestPath)
                .map(path -> path.contains(child)
                        ? getPenWidthString(PENWIDH_THICK)
                        : getPenWidthString(PENWIDTH_THIN));
        return String.format("N%s -> N%s%s;" + LB,
                node.info().name(),
                child.info().name(),
                edgeStyle.orElse(getPenWidthString(PENWIDTH_THIN)));
    }

    @NotNull
    private static String getPenWidthString(String pw) {
        return "[penwidth=" + pw + "]";
    }

    @NotNull
    private String getNodeLabel(Node<S, A> node) {
        var info = node.info();
        String actionName = Optional.ofNullable(info.action())
                .map(Object::toString)
                .orElse("");
        return info.isRoot()
                ? String.format(
                NAME_FORMAT + LB + COUNT_VALUE_FORMAT,
                info.name(), info.count(), info.value())
                :
                String.format(
                        NAME_ACTION_FORMAT + LB + COUNT_VALUE_FORMAT,
                        info.name(), actionName, info.count(), info.value());
    }


}
