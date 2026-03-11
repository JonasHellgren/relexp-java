package chapters.ch10.animation_bandit;

import chapters.ch10.bandit.domain.agent.MemoryBandit;
import chapters.ch10.bandit.domain.trainer.ExperienceBandit;
import java.util.List;

public interface AnimationPolicyI {

    boolean isEmpty();
    void start();

    // events
    void postStep(int ei, int eiMax, List<ExperienceBandit> experiences);
    void postAfterStep(int ei, int eiMax, List<ExperienceBandit> experiences);

    void postEpisode(MemoryBandit memory,int ei, double returnAtT, double[] gradLog, double[] probArray);

}
