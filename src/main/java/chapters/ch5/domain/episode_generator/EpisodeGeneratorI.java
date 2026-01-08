package chapters.ch5.domain.episode_generator;


import chapters.ch5.domain.environment.ExperienceMc;
import chapters.ch5.domain.environment.StateMcI;

import java.util.List;

public interface EpisodeGeneratorI {
    List<ExperienceMc> generate(StateMcI stateStart);
}
