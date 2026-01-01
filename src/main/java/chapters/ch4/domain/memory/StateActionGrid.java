package chapters.ch4.domain.memory;


import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;

/**
 * A record class representing a state-action pair.
 *
 * @param state the state in the grid environment
 * @param action the action taken in that state
 */
public record StateActionGrid(StateGrid state, ActionGrid action) {

    public static StateActionGrid of(StateGrid state, ActionGrid action) {
        return new StateActionGrid(state, action);
    }

    public static StateActionGrid of(int x, int y, ActionGrid action) {
        return new StateActionGrid(StateGrid.of(x,y), action);
    }

    @Override
    public String toString() {
        return "StateActionGrid{" +
                "state=" + state + ", action=" + action +
                '}';
    }

}
