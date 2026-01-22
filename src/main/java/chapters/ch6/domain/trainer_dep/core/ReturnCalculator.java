package chapters.ch6.domain.trainer_dep.core;

import chapters.ch6.domain.trainer_dep.episode_generator.EpisodeInfo;
import com.google.common.base.Preconditions;
import core.gridrl.ExperienceGrid;
import core.gridrl.StateActionGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReturnCalculator {

    @Builder
    record CommonParams(
            int backupHorizon,
            double gamma,
            int stepFutureExp,
            int nStepsEpis,
            EpisodeInfo info,
            int tau
    ) {

        public static CommonParams of(TrainerDependenciesMultiStep dependencies,
                                      int tau,
                                      List<ExperienceGrid> experiences) {
            var info = EpisodeInfo.of(experiences);
            return CommonParams.builder()
                    .backupHorizon(dependencies.trainerParameters().backupHorizon())
                    .gamma(dependencies.trainerParameters().gamma())
                    .stepFutureExp(tau + dependencies.trainerParameters().backupHorizon())
                    .nStepsEpis(info.nSteps())
                    .info(info)
                    .tau(tau)
                    .build();
        }
    }

    TrainerDependenciesMultiStep dependencies;

    public static ReturnCalculator of(TrainerDependenciesMultiStep dependencies) {
        return new ReturnCalculator(dependencies);
    }

    public double calculateReturnAtTau(int tau, List<ExperienceGrid> experiences) {
        checkTau(tau, experiences);
        var c=CommonParams.of(dependencies, tau, experiences);
        double sumRewards = 0;
        sumRewards = returnFromRewardsInHorizon(experiences, c, sumRewards);
        sumRewards = addStateActionValueOfFutureStep(c, sumRewards);
        return sumRewards;
    }


    private static double returnFromRewardsInHorizon(List<ExperienceGrid> experiences,
                                                     CommonParams c,
                                                     double g) {
        for (int k = c.tau; k <= Math.min(c.stepFutureExp -1, c.nStepsEpis -1); k++) {
            var experienceAtK = experiences.get(k);
            g += Math.pow(c.gamma, (k - c.tau)) * experienceAtK.reward();
        }
        return g;
    }

    private double addStateActionValueOfFutureStep(CommonParams c, double G) {
        var agent = dependencies.agent();
        if (!c.info.isIndexOutSide(c.stepFutureExp)) {
            var experienceFuture = c.info.experienceAtTime(c.stepFutureExp);
            var sn = experienceFuture.state();
            var an = experienceFuture.action();
            G += Math.pow(c.gamma, (c.stepFutureExp - c.tau)) * agent.read(StateActionGrid.of(sn, an));
        }
        return G;
    }

    private static void checkTau(int tau, List<ExperienceGrid> experiences) {
        Preconditions.checkArgument(0 <= tau && tau < experiences.size(),
                "tau out of bounds, tau="+tau+", nExperiences="+experiences.size());
    }


}
