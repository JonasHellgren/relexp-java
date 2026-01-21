package chapters.ch6.domain.trainer.core;

import chapters.ch6._shared.info.EpisodeInfo;
import com.google.common.base.Preconditions;
import core.gridrl.ExperienceGrid;
import core.gridrl.StateActionGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReturnCalculator {

    TrainerDependenciesMultiStep dependencies;

    public static ReturnCalculator of(TrainerDependenciesMultiStep dependencies) {
        return new ReturnCalculator(dependencies);
    }

    public double calculateReturnAtTau(int tau, List<ExperienceGrid> experiences) {
        checkTau(tau, experiences);
        int tn = tau + dependencies.trainerParameters().backupHorizon();  //index of future experience
        var info = EpisodeInfo.of(experiences);
        int T = info.nSteps();  //n steps in step
        double gamma = dependencies.trainerParameters().gamma();

        double G = 0;  //sum of rewards
        for (int k = tau; k <= Math.min(tn-1, T-1); k++) {
            var experienceAtK = experiences.get(k);
            G += Math.pow(gamma, (k - tau)) * experienceAtK.reward();
        }

        var agent = dependencies.agent();
        if (!info.isIndexOutSide(tn)) {
            var experienceFuture = info.experienceAtTime(tn);
            var sn = experienceFuture.state();
            var an = experienceFuture.action();
            G += Math.pow(gamma, (tn - tau)) * agent.read(StateActionGrid.of(sn, an));
        }
        return G;
    }


    private static void checkTau(int tau, List<ExperienceGrid> experiences) {
        Preconditions.checkArgument(0 <= tau && tau < experiences.size(),
                "tau out of bounds, tau="+tau+", nExperiences="+experiences.size());
    }


}
