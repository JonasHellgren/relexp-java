package chapters.ch10.bandit.domain.trainer;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record TrainerParametersBandit(int nEpisodes, double learningRate) {
}
