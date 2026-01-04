package chapters.ch11.helper;

import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.trainer.core.ExperienceLunar;
import com.google.common.base.Preconditions;
import core.foundation.util.collections.ListCreator;
import core.foundation.util.collections.MyListUtils;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * Represents information about an step in the lunar lander environment.
 * An step is a sequence of experiences, where each experience consists of a state, an action, and a reward.
 */
@AllArgsConstructor
public class EpisodeInfo {

    private final List<ExperienceLunar> experiences;

    public static EpisodeInfo of(List<ExperienceLunar> experiences) {
        return new EpisodeInfo(experiences);
    }

    public ExperienceLunar startExperience() {
        Preconditions.checkState(nSteps() > 0, "No experiences in step.");
        return experiences.get(0);
    }

    public ExperienceLunar endExperience() {
        return experiences.get(nSteps() - 1);
    }

    public List<Double> rewards() {
        return experiences.stream().map(e -> e.reward()).toList();
    }

    public Double sumRewards() {
        return MyListUtils.sumList(rewards());
    }

    public Integer nSteps() {
        return experiences.size();
    }

    public List<Double> forces(LunarParameters ep) {
        return experiences.stream().map(e -> ep.clippedForce(e.action())).toList();
    }

    public List<Double> accelerations(EnvironmentLunar env) {
        return experiences.stream().map(e -> env.acceleration(e.action())).toList();
    }

    public List<Double> speeds() {
        return experiences.stream().map(e -> e.state().spd()).toList();
    }

    public List<Double> positions() {
        return experiences.stream().map(e -> e.state().y()).toList();
    }

    public List<Double> times(double dt) {
        int nSteps=nSteps();
        return ListCreator.createFromStartWithStepWithNofItems(0, dt,nSteps);
    }

    public ExperienceLunar experienceAtTime(int timeStep) {
        return experiences.get(timeStep);
    }

    public double rewardAtTime(int timeStep) {
        return experiences.get(timeStep).reward();
    }

    public StateLunar stateNewAtTime(int timeStep) {
        return experiences.get(timeStep).stateNew();
    }

    public List<Double> actionsApplied() {
        return experiences.stream().map(e -> e.action()).toList();
    }


}
