package chapters.ch5.implem.splitting;

import chapters.ch5.domain.environment.StateMcI;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/***
 * wraps class StateGrid in temp_diff
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateSplittingMc implements StateMcI {

    StateGrid state;

    public static StateSplittingMc of(int x, int y) {
        return new StateSplittingMc(StateGrid.of(x, y));
    }

    int x() {return state.x();}
    int y() {return state.y();}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var stateMc = (StateSplittingMc) o;
        var stateGrid= SplittingPathAdapter.getStateGrid(stateMc);
        return state.equals(stateGrid);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
