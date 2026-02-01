package chapters.ch13;

import chapters.ch13.domain.searcher.workers.BackPropagator;
import chapters.ch13.domain.searcher.path.Path;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.StateJumper;
import chapters.ch13.factory.FactoryExperienceList;
import chapters.ch13.factory.FactorySearcherSettings;
import chapters.ch13.factory.FactoryTreeForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * (root) -> (newNode) -> (simExp0) -> (simExp1)
 */


class TestBackPropagator {

    public static final List<Double> RETURN_ROOT_AND_CHILD_FOR_THREE_ONE_REWARDS = List.of(3d);
    BackPropagator<StateJumper, ActionJumper> backPropagator;
    Path<StateJumper, ActionJumper> pathNonFail,pathFail;

    @BeforeEach
    void init() {
        var settings = FactorySearcherSettings.forTestClimber();
        backPropagator = BackPropagator.of(settings);
        var tree = FactoryTreeForTest.createClimbingTree();
        var newNode = tree.getRoot().info().children().get(0);
        var treeNodes = List.of(tree.getRoot(), newNode);
        var expListNonFail = FactoryExperienceList.experienceListClimbNonFailEnd(newNode);
        pathNonFail = Path.of(treeNodes, expListNonFail);
        var expListFail = FactoryExperienceList.experienceListClimbFailEnd(newNode);
        pathFail = Path.of(treeNodes, expListFail);
    }


    @Test
      void givenSimulationExperienceEndingInNonFail_thenCorrect() {
        var valuesBefore= pathNonFail.info().valuesTree();
        boolean isDefensiveBackup= backPropagator.update(pathNonFail);
        var valuesAfter= pathNonFail.info().valuesTree();

        Assertions.assertFalse(isDefensiveBackup);
        Assertions.assertTrue(isAllZero(valuesBefore));
        Assertions.assertEquals(pathNonFail.info().lengthNodesTree(), valuesAfter.size());
        Assertions.assertTrue(valuesAfter.containsAll(RETURN_ROOT_AND_CHILD_FOR_THREE_ONE_REWARDS));
      }


    @Test
    void givenSimulationExperienceEndingInFail_thenCorrect() {
        var valuesBefore= pathFail.info().valuesTree();
        boolean isDefensiveBackup= backPropagator.update(pathFail);
        var valuesAfter= pathFail.info().valuesTree();

        Assertions.assertTrue(isDefensiveBackup);
        Assertions.assertTrue(isAllZero(valuesBefore));
        Assertions.assertEquals(pathFail.info().lengthNodesTree(), valuesAfter.size());
        Assertions.assertFalse(valuesAfter.containsAll(RETURN_ROOT_AND_CHILD_FOR_THREE_ONE_REWARDS));
    }


     @Test
      void bothFailAndNonFailBackup() {
         boolean isDefensiveBackupFail= backPropagator.update(pathFail);
         boolean isDefensiveBackupNonFail= backPropagator.update(pathNonFail);
         var valuesAfter= pathFail.info().valuesTree();
         Assertions.assertTrue(isDefensiveBackupFail);
         Assertions.assertFalse(isDefensiveBackupNonFail);
         Assertions.assertTrue(isAllPos(valuesAfter));
      }


    private static boolean isAllZero(List<Double> values) {
        return values.stream().allMatch(element -> element == 0);
    }


    private static boolean isAllPos(List<Double> values) {
        return values.stream().allMatch(element -> element > 0);
    }

}
