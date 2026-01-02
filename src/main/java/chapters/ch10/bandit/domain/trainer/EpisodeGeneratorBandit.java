package chapters.ch10.bandit.domain.trainer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates episodes for a bandit environment using a policy gradient trainer.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EpisodeGeneratorBandit {

    TrainerDependenciesBandit dependencies;

    public static EpisodeGeneratorBandit of(TrainerDependenciesBandit dependencies) {
        return new EpisodeGeneratorBandit(dependencies);
    }

    public List<ExperienceBandit> generate() {
        List<ExperienceBandit> experiences = new ArrayList<>();
        var action= dependencies.chooseAction();
        var stepReturn= dependencies.step(action);
        experiences.add(ExperienceBandit.of(action, stepReturn));
        return experiences;
    }

}
