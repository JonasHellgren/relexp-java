package chapters.ch10.cannon.domain.trainer;

import chapters.ch10.plotting.MeasuresCannon;
import com.google.common.base.Preconditions;
import core.foundation.gadget.math.MeanAndStd;
import core.foundation.gadget.math.LogarithmicDecay;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
        var d=dependencies;
        var generator = EpisodeGeneratorCannon.of(d);
        recorder.clear();
        double base=0;
        for (int i = 0; i < d.nEpisodes(); i++) {
            var experiences = generator.generate();
            for (var exp : experiences) {  //loop through single experience
                int t = experiences.indexOf(exp);
                double returnAtT = getReturnAtTime(experiences, t);
                double lr = d.learningRate(i);
                var gradLog = d.calcGradLog(exp.action());
                d.updateAgentMemory(lr, returnAtT-base, gradLog);
                base=base+lr*(returnAtT-base);
                recorder.addRecording(returnAtT-base, base, exp,gradLog, d.meanAndStd());
            }
        }
    }

    private double getReturnAtTime(List<ExperienceCannon> experiences, int t) {
        Preconditions.checkArgument(t==0, "calculateReturnAtT only works for t=0");
        return experiences.get(t).stepReturn().reward();
    }

}
