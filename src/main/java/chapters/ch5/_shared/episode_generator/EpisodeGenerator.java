package chapters.ch5._shared.episode_generator;

import chapters.ch5.domain.environment.EnvironmentMcI;
import chapters.ch5.domain.environment.ExperienceMc;
import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.domain.policy.PolicyMcI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * This class generates episodes for a given environment and policy.
 * An step is a sequence of experiences, where each experience consists of a state, an action, and a reward.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EpisodeGenerator implements EpisodeGeneratorI {

    EnvironmentMcI environment;
    PolicyMcI policy;

    public static EpisodeGenerator of(EnvironmentMcI environment, PolicyMcI policy) {
        return new EpisodeGenerator(environment, policy);
    }

    /**
     * Generates an step starting from the given initial state.
     *
     * @param stateStart the initial state of the step
     * @return a list of experiences in the step
     */
    @Override
    public List<ExperienceMc> generate(StateMcI stateStart) {
        List<ExperienceMc> experiences = new ArrayList<>();
        var state = stateStart;
        boolean isTerminal;
        do {
            var action = policy.chooseAction(state);
            var sr = environment.step(state, action);
            experiences.add(ExperienceMc.of(state, action, sr));
            state = sr.sNext();
            isTerminal = sr.isTerminal();
        } while (!isTerminal);
        return experiences;
    }

}
