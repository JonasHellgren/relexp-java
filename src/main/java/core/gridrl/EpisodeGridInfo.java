package core.gridrl;

import core.foundation.util.collections.MyListUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Represents information about an step in a grid environment.
 * An step is a sequence of experiences, where each experience consists of a state, an action, and a reward.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EpisodeGridInfo {

    private final List<ExperienceGrid> experiences;

    public static EpisodeGridInfo of(List<ExperienceGrid> experiences) {
        return new EpisodeGridInfo(experiences);
    }

    public int nExperiences() {
        return experiences.size();
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

}
