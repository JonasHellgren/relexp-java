package chapters.ch4.domain.animation;

import core.gridrl.AgentGridI;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;

public interface AnimationGridI {

    // state
    boolean isEmpty();

    // lifecycle
    void start();

    // events
    void postStep(StateGrid s, int ei, int eiMax, double pRand, double reward);
    void postEpisode(AgentGridI agent, EnvironmentGridI env0);
}
