package chapters.ch13.factory;

import k_mcts.domain.environment.Experience;
import k_mcts.domain.searcher.node.Node;
import k_mcts.environments.jumper.ActionJumper;
import k_mcts.environments.jumper.EnvironmentJumper;
import k_mcts.environments.jumper.StateJumper;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class FactoryExperienceList {

    public static final ActionJumper UP = ActionJumper.up;
    public static final ActionJumper DOWN = ActionJumper.n;


    public static List<Experience<StateJumper, ActionJumper>> experienceListClimbNonFailEnd(
            Node<StateJumper, ActionJumper> newNode) {
        List<Experience<StateJumper, ActionJumper>> expList = new ArrayList<>();
        var environment = EnvironmentJumper.create();
        var stepReturn0 = environment.step(newNode.info().state(), UP);
        expList.add(Experience.of(newNode.info().state(), UP, stepReturn0));
        var stepReturn1 = environment.step(stepReturn0.stateNew(), UP);
        expList.add(Experience.of(stepReturn0.stateNew(), UP, stepReturn1));
        return expList;
    }

    public static List<Experience<StateJumper, ActionJumper>> experienceListClimbFailEnd(
            Node<StateJumper, ActionJumper> newNode) {
        List<Experience<StateJumper, ActionJumper>> expList = new ArrayList<>();
        var environment = EnvironmentJumper.create();
        var stepReturn0 = environment.step(newNode.info().state(), UP);
        expList.add(Experience.of(newNode.info().state(), UP, stepReturn0));
        var stepReturn1 = environment.step(stepReturn0.stateNew(), DOWN);
        expList.add(Experience.of(stepReturn0.stateNew(), UP, stepReturn1));
        return expList;
    }

}
