package chapters.ch10.bandit.domain.trainer;

import chapters.ch10.plotting.MeasuresBandit;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * This class is responsible for training an agent using policy gradient methods.
 * It generates episodes, calculates returns, and updates the agent's memory.
 * <p>
 * while training termination criteria is false
 * generate an step, save all experiences (multiple <s,a,r,s’>)
 * for each step t of the step
 * G(t) ← ∑_(k=t)^(ne-1)▒〖γ^(t-k)∙r(k)〗
 * θ←θ+α∙G(t)∙∇log⁡π(a(t)│s(t),θ)
 * end
 * end
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TrainerBandit {

    private TrainerDependenciesBandit dependencies;
    private RecorderBandit recorder;

    public static TrainerBandit of(TrainerDependenciesBandit dependencies) {
        return new TrainerBandit(dependencies, RecorderBandit.empty());
    }

    public void train() {
        var d = dependencies;
        var generator = EpisodeGeneratorBandit.of(dependencies);
        recorder.clear();
        for (int i = 0; i < d.nEpisodes(); i++) {
            var experiences = generator.generate();
            for (var experience : experiences) {  //looping through single experience
                int t = experiences.indexOf(experience);
                double returnAtT = getReturnAtTime(experiences, t);
                var probs = d.actionProbabilities();
                double lr = d.learningRate();
                var gradLog = d.calculateGradLog(experience, probs);
                d.updateAgentMemory(lr, returnAtT, gradLog);
                addRecording(experiences, gradLog, probs);
            }
        }
    }

    private double getReturnAtTime(List<ExperienceBandit> experiences, int t) {
        Preconditions.checkArgument(t == 0, "calculateReturnAtT only works for t=0");
        return experiences.get(t).stepReturn().reward();
    }


    private void addRecording(List<ExperienceBandit> experiences, double[] gradLog, double[] probArray) {
        var measures = MeasuresBandit.getMeasures(experiences, gradLog, probArray);
        recorder.add(measures);
    }

}
