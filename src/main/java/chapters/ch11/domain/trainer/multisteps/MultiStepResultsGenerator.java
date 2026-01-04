package chapters.ch11.domain.trainer.multisteps;

import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.trainer.core.ExperienceLunar;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.helper.EpisodeInfo;
import chapters.ch11.helper.ValueCalculatorLunar;
import com.google.common.base.Preconditions;
import core.learningutils.MyRewardListUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/***
 * Used to derive return (sum rewards) at tStart for given results
 *  n-step return si Gk:k+n=R(k)...gamma^(n-1)*R(k+n-1)+gamma^n*V(S(k+n-1))
 *  k is referring to experience index
 *  therefore
 *  Gk=(k=0,1=2, gamma=1)=R0+V(sNext(0))   (standard TD)
 *  Gk=(k=0,n=2, gamma=1)=R0+R1+V(sNext(1))
 *  A basic principle is that reward for stepping into terminal is included but value
 *  of terminal sNext is zero
 */


@Log
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiStepResultsGenerator {

    TrainerDependencies dependencies;
    ValueCalculatorLunar calculator;

    public static MultiStepResultsGenerator of(TrainerDependencies dependencies) {
        return new MultiStepResultsGenerator(dependencies, ValueCalculatorLunar.of(dependencies));
    }

    public MultiStepResults generate(List<ExperienceLunar> experiences) {
        var informer = EpisodeInfo.of(experiences);
        var results = MultiStepResults.create(informer.nSteps());
        IntStream.range(0, informer.nSteps())
                .mapToObj(time -> createResultAtTime(time, informer))
                .forEach(results::add);
        return results;
    }

    private MultiStepResult createResultAtTime(int time, EpisodeInfo informer) {
        int nExperiences = informer.nSteps();
        var parameters = dependencies.trainerParameters();
        int idxEnd = time + parameters.nStepsHorizon();
        validate(time, nExperiences, idxEnd);
        var rewards = getRewards(time, informer, idxEnd, nExperiences);
        double rewardSum = MyRewardListUtils.discountedSum(rewards, parameters.gamma());
        boolean isEndOutSide = idxEnd > nExperiences - 1;
        var stateFut = getStateFutureOptional(informer, isEndOutSide, idxEnd);
        double valueTarget = stateFut.map(
                state -> calculator.valueOfTakingAction(state, rewardSum))
                .orElse(rewardSum);
        var e = informer.experienceAtTime(time);
        double advantage = calculator.calcAdvantage(dependencies.agent(), e.state(), valueTarget);
        double tdError = calculator.calcTemporalDifferenceError(e);
        return MultiStepResult.builder()
                .state(e.state())
                .action(e.action())
                .sumRewards(rewardSum)
                .stateFuture(stateFut)
                .valueTarget(valueTarget)
                .advantage(advantage)
                .tdError(tdError)
                .build();
    }

    private static void validate(int time, int nExperiences, int idxEnd) {
        Preconditions.checkArgument(time < nExperiences, "Non valid start index, time=" + time);
        Preconditions.checkArgument(idxEnd > time, "Non valid end index, idxEnd=" + idxEnd + ", time=" + time);
    }

    /***
     * Rewards are based on [time, idxEnd - 1], where idxEnd=time+backupHorizon
     * Example backupHorizon = 1 => [time, time] => one reward
     * Example backupHorizon = 2 => [time, time+1] => two rewards (if time+1 is in step)
     */

    private static List<Double> getRewards(int time, EpisodeInfo informer, int idxEnd, int nExperiences) {
        return IntStream.rangeClosed(time, Math.min(idxEnd - 1, nExperiences - 1))  //end inclusive
                .mapToObj(t -> informer.rewardAtTime(t)).toList();
    }

    /***
     *  idxEnd=time+backupHorizon  => stateFuture(time+backupHorizon)=stateFuture(idxEnd)=stateNewAtTime(idxEnd - 1)
     *  stateNewAtTime is the  new state when transitioning from state at time t, therefore -1
     */

    private static Optional<StateLunar> getStateFutureOptional(
            EpisodeInfo informer, boolean isEndStateOutSide, int idxEnd) {
        return isEndStateOutSide
                ? Optional.empty()
                : Optional.of(informer.stateNewAtTime(idxEnd - 1));
    }



}
