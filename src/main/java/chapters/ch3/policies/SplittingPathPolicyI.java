package chapters.ch3.policies;


import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;

public interface SplittingPathPolicyI {
    ActionGrid chooseAction(StateGrid s);
}
