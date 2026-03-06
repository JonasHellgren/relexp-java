package chapters.ch9.animation;

import chapters.ch10.bandit.domain.agent.MemoryBandit;
import chapters.ch10.bandit.domain.trainer.ExperienceBandit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimationPolicyEmpty implements AnimationPolicyI {

    public static AnimationPolicyI create() {
        return new AnimationPolicyEmpty();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void start() {

    }

    @Override
    public void postStep(int ei, int eiMax, List<ExperienceBandit> experiences) {

    }

    @Override
    public void postEpisode(MemoryBandit memory, int ei, double returnAtT, double[] gradLog, double[] probArray) {

    }
}
