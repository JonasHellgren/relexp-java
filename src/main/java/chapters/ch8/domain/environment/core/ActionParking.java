package chapters.ch8.domain.environment.core;

import core.foundation.util.rand.RandUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
/**
 * Enum representing the possible actions in parking environment.
 * Each action is associated with a symbol.
 */

@Getter
@AllArgsConstructor
public enum ActionParking {
     REJECT("❌","R"),ACCEPT("✅","A");

    private final String symbol;
    private final String letter;

    public static List<ActionParking> allActions() {
        return List.of(ActionParking.ACCEPT, ActionParking.REJECT);
    }

    public static int maxNofActions() {
        return allActions().size();
    }

    public static ActionParking random() {
        return allActions().get(RandUtils.getRandomIntNumber(0, maxNofActions()));
    }

    public ActionParking copy() {
        return this;
    }

    @Override
    public String toString() {
        return symbol;
    }


}
