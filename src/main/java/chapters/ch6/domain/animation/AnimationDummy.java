package chapters.ch6.domain.animation;

import chapters.ch6.domain.agent.AgentGridMultiStepI;
import core.gridrl.AgentGridI;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;

public class AnimationDummy implements AnimationGridMultiStepI {


    public static AnimationDummy empty() {
        return new AnimationDummy();
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
