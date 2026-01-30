package chapters.ch12.domain.inv_pendulum.agent.memory;

import chapters.ch12.domain.inv_pendulum.environment.core.ActionPendulum;
import java.util.List;
import java.util.Objects;

/***
 *  This value object is handy when we want to keep track of  target values for actions
 *  Used by TargetCalculator
 */

public record ActionAndItsValue(
        ActionPendulum a,
        double actionValue) {

    public static ActionAndItsValue of(ActionPendulum a, double actionValue) {
        return new ActionAndItsValue(a, actionValue);
    }

    /**
     * Finds the ActionAndItsValue instance in the given list that corresponds to the given action.
     *
     * @param a the action to find
     * @param avList the list of ActionAndItsValue instances to search
     * @return the ActionAndItsValue instance corresponding to the given action, or throws an exception if not found
     */
    public static ActionAndItsValue findActionValue(ActionPendulum a, List<ActionAndItsValue> avList) {
        return avList.stream()
                .filter(av -> av.a().equals(a))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ActionAndItsValue that = (ActionAndItsValue) obj;
        return Double.compare(that.actionValue, actionValue) == 0 && Objects.equals(a, that.a);
    }

}
