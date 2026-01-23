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
        var generator = EpisodeGeneratorCannon.of(dependencies);
        var agent = dependencies.agent();
        var calculator= GradLogCalculatorContinuousAction.of(dependencies.parameters());
        var tp=dependencies.parameters();
        var learningRate = LogarithmicDecay.of(
                tp.learningRateStart(),
                tp.learningRateEnd(),
                tp.nEpisodes());
        recorder.clear();
        double base=0;
        for (int i = 0; i < dependencies.nEpisodes(); i++) {
            var experiences = generator.generate();
            for (var experience : experiences) {
                double returnAtT = getReturnAtT(experience, experiences);
                double lr = learningRate.calcOut(i);
                var gradLog = calculator.gradLog(experience.action(), agent.meanAndStd());
                agent.updateMemory(lr, returnAtT-base, gradLog);
                base=base+lr*(returnAtT-base);
                addRecording(returnAtT-base, base, experience,gradLog, agent.meanAndStd());
            }
        }
    }

    private double getReturnAtT(ExperienceCannon experience, List<ExperienceCannon> experiences) {
        int t = experiences.indexOf(experience);
        return calculateReturnAtT(experiences, t);
    }

    private double calculateReturnAtT(List<ExperienceCannon> experiences, int t) {
        Preconditions.checkArgument(t==0, "calculateReturnAtT only works for t=0");
        return experiences.get(t).stepReturn().reward();
    }

    private void addRecording(double gMinusBase, double base,
                              ExperienceCannon experience,
                              GradientMeanAndLogStd gradLog,
                              MeanAndStd meanAndStd) {
        var measures= MeasuresCannon.builder()
                .returnMinusBase(gMinusBase).base(base)
                .angle(experience.action()).distance(experience.stepReturn().distance())
                .gradLogZMean(gradLog.mean()).gradLogZStd(gradLog.std())
                .mean(meanAndStd.mean()).std(meanAndStd.std())
               .build();
        recorder.add(measures);
    }


}
