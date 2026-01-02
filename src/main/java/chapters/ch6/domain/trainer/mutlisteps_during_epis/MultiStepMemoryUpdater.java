package chapters.ch6.domain.trainer.mutlisteps_during_epis;

import chapters.ch4.domain.memory.StateActionGrid;
import chapters.ch4.domain.trainer.core.ExperienceGrid;
import chapters.ch6._shared.info.EpisodeInfo;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;


/**
 * This class is responsible for updating the agent's memory using multi-step Q-learning.
 * It calculates the return at a given time step tau and updates the agent's memory accordingly.

 //pseudo code:
 * tn ←tau+n
 * T ← length(experiences)
 * s, r ← extract from experience at index tau
 * G(tau) ← ∑_(k=tau)^(min⁡(tn-1,T-1))▒〖γ^(t-k)∙r(k)〗
 * sn ← the state n steps ahead from s
 * if sn is present
 *   an ← a’ for experience at index tn
 *   G(t) ← G(t)+ γ^n·Q(sn,an)
 * Q(s,a)← Q(s,a)+α·(G-Q(s,a))
 *
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MultiStepMemoryUpdater {

    private final TrainerDependenciesMultiStep dependencies;

    public static MultiStepMemoryUpdater of(TrainerDependenciesMultiStep dependencies) {
        return new MultiStepMemoryUpdater(dependencies);
    }

    /**
     * Updates the agent's memory using multi-step Q-learning.
     *
     * @param tau the time step at which to update the memory
     * @param experiences the list of experiences
     * @param learningRate the learning rate
     */
    public void updateAgentMemory(int tau, List<ExperienceGrid> experiences, double learningRate) {
        checkTau(tau, experiences);
        double sumOfRewards = calculateReturnAtTau(tau, experiences);
        updateAgentMemory(experiences.get(tau), sumOfRewards, learningRate);
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

    private void updateAgentMemory(ExperienceGrid experienceAtTau, double sumOfRewards, double learningRate) {
        var agent = dependencies.agent();
        var s = experienceAtTau.state();
        var a = experienceAtTau.action();
        double saValue = agent.read(StateActionGrid.of(s, a));
        double valueTar = saValue + learningRate * (sumOfRewards - saValue);
        agent.getMemory().write(StateActionGrid.of(s, a), valueTar);
    }


}
