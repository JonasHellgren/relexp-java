package chapters.ch10.bandit._shared;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record MeasuresBandit(
        double sumRewards,
        double probLeft,
        double probRight,
        double gradLogLeft,
        double gradLogRight
) {
}
