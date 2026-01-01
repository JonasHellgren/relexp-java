package chapters.ch4.domain.helper;

import chapters.ch4.domain.trainer.core.ExperienceGrid;
import core.foundation.util.list_array.MyListUtils;
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
