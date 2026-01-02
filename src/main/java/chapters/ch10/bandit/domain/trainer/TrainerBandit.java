package chapters.ch10.bandit.domain.trainer;

import chapters.ch10.bandit._shared.GradLogCalculatorDiscreteActions;
import chapters.ch10.bandit._shared.MeasuresBandit;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 *
 *  This class is responsible for training an agent using policy gradient methods.
 *  It generates episodes, calculates returns, and updates the agent's memory.
 *
 * while training termination criteria is false
 *  generate an step, save all experiences (multiple <s,a,r,s’>)
 *  for each step t of the step
 *   G(t) ← ∑_(k=t)^(ne-1)▒〖γ^(t-k)∙r(k)〗
 *   θ←θ+α∙G(t)∙∇log⁡π(a(t)│s(t),θ)
 *  end
 * end
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TrainerBandit {

    TrainerDependenciesBandit dependencies;
    RecorderBandit recorder;

    public static TrainerBandit of(TrainerDependenciesBandit dependencies) {
        return new TrainerBandit(dependencies,RecorderBandit.empty());
    }

    public void train() {
        var generator = EpisodeGeneratorBandit.of(dependencies);
        var agent = dependencies.agent();
        recorder.clear();
        for (int i = 0; i < dependencies.nEpisodes(); i++) {
            var experiences = generator.generate();
            for (var experience : experiences) {
                double returnAtT = getReturnAtT(experience, experiences);
                var probs = agent.actionProbabilities();
                double learningRate = dependencies.trainerParameters().learningRate();
                var gradLog = GradLogCalculatorDiscreteActions.calc(experience.action().getIndex(), probs);
                agent.updateMemory(learningRate, returnAtT, gradLog);
                addRecording(experiences, gradLog, probs);
            }
        }

    }

    private double getReturnAtT(ExperienceBandit experience, List<ExperienceBandit> experiences) {
        int t = experiences.indexOf(experience);
        return calculateReturnAtT(experiences, t);
    }

    private double calculateReturnAtT(List<ExperienceBandit> experiences, int t) {
        Preconditions.checkArgument(t==0, "calculateReturnAtT only works for t=0");
        return experiences.get(t).stepReturn().reward();
    }

    private void addRecording(List<ExperienceBandit> experiences, double[] gradLog, double[] probArray) {
        var measures= MeasuresBandit.builder()
                .sumRewards(experiences.get(0).stepReturn().reward())
                .gradLogLeft(gradLog[0]).gradLogRight(gradLog[1])
                .probLeft(probArray[0]).probRight(probArray[1])
                .build();
        recorder.add(measures);
    }

}
