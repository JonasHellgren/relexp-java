package chapters.ch6.domain.animation;

import chapters.ch6.domain.agent.AgentGridMultiStepI;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;

public class AnimationGridMsEmpty implements AnimationGridMultiStepI {


    public static AnimationGridMsEmpty create() {
        return new AnimationGridMsEmpty();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void start() {
    }

    @Override
    public void postStep(StateGrid s, int ei, int eiMax, double pRand, double reward) {

    }

    @Override
    public void postEpisode(AgentGridMultiStepI agent, EnvironmentGridI env0,int ei) {
    }

}
