package chapters.ch6.domain.animation;

import chapters.ch6.domain.agent.AgentGridMultiStepI;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;

public interface AnimationGridMultiStepI {

    // state
    boolean isEmpty();

    // lifecycle
    void start();

    // events
    void postStep(StateGrid s, int ei, int eiMax, double pRand, double reward);
    void postEpisode(AgentGridMultiStepI agent, EnvironmentGridI env0,int ei);
}
