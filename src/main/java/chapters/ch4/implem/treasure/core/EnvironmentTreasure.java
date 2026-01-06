package chapters.ch4.implem.treasure.core;

import chapters.ch4.domain.param.InformerGridParamsI;
import core.gridrl.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Implementation of the EnvironmentGridI interface for the Treasure environment.
 * This class provides the logic for stepping through the environment, given a state and an action.
 */
@AllArgsConstructor
@Getter
public class EnvironmentTreasure implements EnvironmentGridI {

    public static final String NAME="Treasure";
    private final InformerTreasure informer;

    public static EnvironmentTreasure of(EnvironmentParametersTreasure parameters) {
        return new EnvironmentTreasure(InformerTreasure.create(parameters));
    }

    @Override
    public String name() {
        return NAME;
    }

    /**
     * Takes a step in the environment, given a state and an action.
     *
     * @param s the current state of the environment
     * @param a the action to take
     * @return the result of taking the action in the current state
     */
    @Override
    public StepReturnGrid step(StateGrid s, ActionGrid a) {
        informer.validateStateAndAction(s,a);
        var sNext = getNextState(s, a);
        var isFail = informer.isFail(sNext);
        var isTerminal = informer.isTerminalNonFail(sNext) || isFail;
        var reward = calculateReward(sNext,isTerminal);
        return StepReturnGrid.builder()
                .sNext(sNext)
                .reward(reward)
                .isFail(isFail)
                .isTerminal(isTerminal)
                .build();
    }

    @Override
    public InformerGridParamsI informer() {
        return informer;
    }

    private StateGrid getNextState(StateGrid s, ActionGrid a) {
        var xyMin=informer.xyMin();
        var xyMax=informer.xyMax();
        var sAfterActionApplied = s.ofApplyingAction(a).clip(xyMin,xyMax);
        return stateNotPositionedAtWall(s, sAfterActionApplied);
    }

    private StateGrid stateNotPositionedAtWall(StateGrid s, StateGrid sAfterActionApplied) {
        return informer.isWall(sAfterActionApplied) ? s : sAfterActionApplied;
    }

    private double calculateReward(StateGrid sNext, boolean isTerminal) {
        double rTerminal=isTerminal ? informer.rewardAtTerminalPos(sNext):0;
        return rTerminal+informer.rewardMove();
    }



}
