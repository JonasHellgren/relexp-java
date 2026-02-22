package chapters.ch4.domain.animation;

import core.gridrl.AgentGridI;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;

public class AnimationDummy implements AnimationGridI{


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
    public void postEpisode(AgentGridI agent, EnvironmentGridI env0) {

    }
}
