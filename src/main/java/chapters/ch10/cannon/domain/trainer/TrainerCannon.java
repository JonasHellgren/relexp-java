package chapters.ch10.cannon.domain.trainer;

import chapters.ch10.animation.cannon.AnimationCannon;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.math3.util.Pair;

import java.util.List;

/**
 * This class is responsible for training an agent using policy gradient methods.
 * It generates episodes, calculates returns, and updates the agent's memory.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TrainerCannon {

    TrainerDependenciesCannon dependencies;
    RecorderCannon recorder;

    public static TrainerCannon of(TrainerDependenciesCannon dependencies) {
        return new TrainerCannon(dependencies, RecorderCannon.empty());
    }

    public void train() {
        train(AnimationCannon.empty());
    }

    public void train(AnimationCannon animation) {
        var d = dependencies;
        var generator = EpisodeGeneratorCannon.of(d);
        recorder.clear();
        animation.start();
        double base = 0;
        for (int i = 0; i < d.nEpisodes(); i++) {
            var experiences = generator.generate();
            for (int t = 0; t < experiences.size(); t++) {
                var exp = experiences.get(t);
                double returnAtT = getReturnAtTime(experiences, t);
                double lr = d.learningRate(i);
                var gradLog = d.calcGradLog(exp.action());

                recorder.addRecording(returnAtT - base, base, exp, gradLog, d.meanAndStd());
                animation.postFire(i, d.nEpisodes(), experiences);
                animation.postHit(i, d.nEpisodes(), experiences);
                animation.postEpisode(d.agentmemory(), i, Pair.create(returnAtT, base), gradLog);

                d.updateAgentMemory(lr, returnAtT - base, gradLog);
                base = base + lr * (returnAtT - base);
            }
        }
    }

    private double getReturnAtTime(List<ExperienceCannon> experiences, int t) {
        Preconditions.checkArgument(t == 0, "calculateReturnAtT only works for t=0");
        return experiences.get(t).stepReturn().reward();
    }

}
