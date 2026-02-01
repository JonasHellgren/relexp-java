package ch13;

import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.searcher.path.OptimalPathExtractor;
import chapters.ch13.domain.searcher.core.Dependencies;
import chapters.ch13.domain.searcher.core.Searcher;
import chapters.ch13.domain.tree.Tree;
import chapters.ch13.factory.jumper.FactoryDependenciesJumper;
import chapters.ch13.factory.jumper.RunnerSettings;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.StateJumper;
import chapters.ch13.factory.jumper.FactoryTreeJumper;
import chapters.ch13.plotting.DotFileGenerator;
import core.foundation.config.ConfigFactory;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.gadget.timer.CpuTimer;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Map;

/***
 *
 * For generating file with graph (dot file), type (also printed when running)
 *  dot -Tpng filePath.dot -o filePath.png
 */


public class RunnerSearcherJumper {


    enum SETTING {HIGHEXP_NOTDEFBACKUP, HIGHEXP_DEFBACKUP, LOWEXP_NOTDEFBACKUP, LOWEXP_DEFBACKUP}
    enum NOFITERATIONS {ONE, TWO, FIVE, HUNDRED}

    static final String FILE_NAME0 = "climb_mcts";
    static final String FILE_END = ".dot";

    static final Pair<NOFITERATIONS, SETTING> TEST_SETUP =
        //    Pair.create(NOFITERATIONS.FIVE, SETTING.LOWEXP_NOTDEFBACKUP);
            Pair.create(NOFITERATIONS.HUNDRED, SETTING.HIGHEXP_NOTDEFBACKUP);

    static Map<NOFITERATIONS, Integer> nofIterations = Map.of(
            NOFITERATIONS.ONE, 1,
            NOFITERATIONS.TWO, 2,
            NOFITERATIONS.FIVE, 5,
            NOFITERATIONS.HUNDRED, 100
    );

    static Map<SETTING, RunnerSettings> settings = Map.of(
            SETTING.LOWEXP_NOTDEFBACKUP, RunnerSettings.of(2, 1.0, 1.0),
            SETTING.LOWEXP_DEFBACKUP, RunnerSettings.of(2, 0.5, 0.1),
            SETTING.HIGHEXP_DEFBACKUP, RunnerSettings.of(2, 0.5, 0.1),
            SETTING.HIGHEXP_NOTDEFBACKUP, RunnerSettings.of(10, 1.0, 1.0)
    );

    public static void main(String[] args) throws IOException {
        var dependencies = FactoryDependenciesJumper.runner(
                nofIterations.get(TEST_SETUP.getFirst()),
                settings.get(TEST_SETUP.getSecond()));
        var searcher = Searcher.of(dependencies);
        var root = FactoryTreeJumper.onlyRoot();
        var tree = searcher.search(root);
        searcher.logTime();
        var filePath = createDotFile(dependencies, root, tree);
        printInConsole(tree, filePath);
    }

    private static void printInConsole(Tree<StateJumper, ActionJumper> tree, String filePath) {
        System.out.println("tree = " + tree);
        System.out.println("dot -Tpng " + filePath +".dot" + " -o " + filePath +".png");
    }

    @NotNull
    private static String createDotFile(Dependencies<StateJumper, ActionJumper> dependencies,
                                        Node<StateJumper, ActionJumper> root,
                                        Tree<StateJumper, ActionJumper> tree) throws IOException {
        var pathExtractor = OptimalPathExtractor.of(dependencies);
        var generator = DotFileGenerator.init();
        var nodes = pathExtractor.extract(root).getNodes();
        String text = generator.generateDot(tree, nodes);
        var path = ConfigFactory.pathPicsConfig().ch13();
        String filePath = path + FILE_NAME0 + TEST_SETUP.getFirst()+"iter"+ TEST_SETUP.getSecond();
        generator.writeToFile(filePath+ FILE_END, text);
        return filePath;
    }


}
