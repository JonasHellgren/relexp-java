package chapters.ch8.domain.agent.memory;

import chapters.ch8.domain.agent.core.ExperienceParking;
import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.StateParking;
import java.util.Objects;

/**
 * Represents a state-action pair in the parking environment.
 * This record class is used to store and manipulate state-action pairs.
 */
public record StateActionParking(StateParking state, ActionParking action) {

    public static StateActionParking of(StateParking state, ActionParking action) {
        return new StateActionParking(state, action);
    }

    public static StateActionParking ofExp(ExperienceParking exp) {
        return new StateActionParking(exp.state(), exp.action());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateActionParking that = (StateActionParking) o;
        return Objects.equals(state, that.state) && Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state.hashCode(), action.hashCode());
    }


    @Override
    public String toString() {
        return "StateActionParking{" +
                "state=" + state + ", action=" + action +
                '}';
    }

}
