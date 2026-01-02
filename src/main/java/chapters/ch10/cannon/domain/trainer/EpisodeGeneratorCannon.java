package chapters.ch10.cannon.domain.trainer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates episodes for a cannon environment using a policy gradient trainer.
 * An step is a sequence of experiences, where each experience consists of a state, an action, and a reward.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EpisodeGeneratorCannon {

    TrainerDependenciesCannon dependencies;

    public static EpisodeGeneratorCannon of(TrainerDependenciesCannon dependencies) {
        return new EpisodeGeneratorCannon(dependencies);
    }

    public List<ExperienceCannon> generate() {
        List<ExperienceCannon> experiences = new ArrayList<>();
        var action = dependencies.chooseAction();
        var stepReturn = dependencies.step(action);
        experiences.add(ExperienceCannon.of(action, stepReturn));
        return experiences;
    }

}
