package chapters.ch6._shared.info;

import core.gridrl.StateActionGrid;
import core.gridrl.ExperienceGrid;
import core.foundation.util.collections.MyListUtils;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Represents information about an step in a grid environment.
 * An step is a sequence of experiences, where each experience consists of a state,
 * an action, and a reward.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EpisodeInfo {

    private final List<ExperienceGrid> experiences;

    public static EpisodeInfo of(List<ExperienceGrid> experiences) {
        return new EpisodeInfo(experiences);
    }

    public ExperienceGrid endExperience() {
        return experiences.get(nSteps() - 1);
    }

    public List<Double> rewards() {
        return experiences.stream().map(e -> e.reward()).toList();
    }

    public Double sumRewards() {
        return MyListUtils.sumList(rewards());
    }

    public Integer nSteps() {
        return experiences.size();
    }

    public ExperienceGrid experienceAtTime(int timeStep) {
        return experiences.get(timeStep);
    }

    public double rewardAtTime(int timeStep) {
        return experiences.get(timeStep).reward();
    }

    public StateGrid stateNewAtTime(int timeStep) {
        return experiences.get(timeStep).stateNext();
    }

    public Optional<ActionGrid> actionNewAtTime(int timeStep) {
        return experiences.get(timeStep).actionNext();
    }

    public  boolean isFirstVisit(StateActionGrid sa,
                                 int listPosition) {
        int iFirst = IntStream.range(0, experiences.size())
                .filter(i -> experiences.get(i).state().equals(sa.state()) &&
                        experiences.get(i).action().equals(sa.action()))
                .findFirst()
                .orElse(0);
        return listPosition == iFirst;
    }


    /***
     * Rewards are based on [time, idxEnd - 1], where idxEnd=time+backupHorizon
     * Example backupHorizon = 1 => [time, time] => one reward
     * Example backupHorizon = 2 => [time, time+1] => two rewards (if time+1 is in step)
     */

    public  List<Double> getRewards(int time,  int idxEnd) {
        int nExperiences=nSteps();
        return IntStream.rangeClosed(time, Math.min(idxEnd - 1, nExperiences - 1))  //end inclusive
                .mapToObj(t -> rewardAtTime(t)).toList();
    }

    public Optional<StateGrid> getStateFutureOptional(int idxEnd) {
        return isIndexOutSide(idxEnd)
                    ? Optional.empty()
                    : Optional.of(stateNewAtTime(idxEnd - 1));
    }

    public Optional<ActionGrid> getActionFutureOptional(int idxEnd) {
        return isIndexOutSide(idxEnd)
                ? Optional.empty()
                : actionNewAtTime(idxEnd - 1);
    }


    public boolean isIndexOutSide(int idxEnd) {
        int nExperiences=nSteps();
        return idxEnd > nExperiences - 1;
    }



}
