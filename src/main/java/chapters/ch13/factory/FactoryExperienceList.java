package chapters.ch13.factory;

import chapters.ch13.domain.environment.Experience;
import chapters.ch13.domain.searcher.node.Node;
import chapters.ch13.environments.jumper.ActionJumper;
import chapters.ch13.environments.jumper.EnvironmentJumper;
import chapters.ch13.environments.jumper.StateJumper;
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
