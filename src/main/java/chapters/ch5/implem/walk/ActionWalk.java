package chapters.ch5.implem.walk;

import chapters.ch5.domain.environment.ActionMcI;
import core.foundation.util.rand.RandUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ActionWalk implements ActionMcI {
    W(WalkActionProperties.of(-1,"←")),
    E(WalkActionProperties.of(1,"→"));

    private final WalkActionProperties properties;

    public static List<ActionWalk> getAllActions() {
        return List.of(ActionWalk.W, ActionWalk.E);
    }

    public static int maxNofActions() {
        return getAllActions().size();
    }

    public static ActionMcI random() {
        int randomIntNumber = RandUtils.getRandomIntNumber(0, maxNofActions());
        return getAllActions().get(randomIntNumber);
    }


    public int deltaX() {return properties.deltaX();}
    public String arrow() {return properties.arrow();}

    @Override
    public String toString() {
        return properties.arrow();
    }
}
