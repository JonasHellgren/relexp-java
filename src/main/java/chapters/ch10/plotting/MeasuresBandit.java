package chapters.ch10.plotting;

import chapters.ch10.bandit.domain.trainer.ExperienceBandit;
import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder
@With
public record MeasuresBandit(
        double sumRewards,
        double probLeft,
        double probRight,
        double gradLogLeft,
        double gradLogRight
) {



    public static MeasuresBandit getMeasures(List<ExperienceBandit> experiences,
                                             double[] gradLog,
                                             double[] probArray) {
        Preconditions.checkArgument(experiences.size() == 1,"single experience in bandit problems");
        return MeasuresBandit.builder()
                .sumRewards(experiences.get(0).stepReturn().reward())
                .gradLogLeft(gradLog[0]).gradLogRight(gradLog[1])
                .probLeft(probArray[0]).probRight(probArray[1])
                .build();
    }


}
