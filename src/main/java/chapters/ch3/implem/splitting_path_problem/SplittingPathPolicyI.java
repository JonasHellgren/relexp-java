package chapters.ch3.implem.splitting_path_problem;


import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;

public interface SplittingPathPolicyI {
    ActionGrid chooseAction(StateGrid s);
}
