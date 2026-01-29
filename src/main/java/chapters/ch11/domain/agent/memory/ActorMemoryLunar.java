package chapters.ch11.domain.agent.memory;


import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.trainer.param.TrainerParameters;
import chapters.ch11.factory.RbfMemoryFactory;
import chapters.ch11.helper.RadialBasisAdapter;
import core.foundation.gadget.math.MeanAndLogStd;
import core.foundation.gadget.math.MeanAndStd;
import core.foundation.gadget.training.TrainData;
import core.foundation.util.collections.ArrayCreatorUtil;
import core.nextlevelrl.radial_basis.RbfNetwork;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the memory of an actor in the lunar lander domain.
 * This class is responsible for storing and updating the mean and standard deviation of the actor's output.
 * The memory stores the mean and log of standard deviation of the actor's output for each state in the environment.
 * Storing log is more efficient than storing the actual standard deviation. One reason is lower variance another
 * is simpler gradient calculation.
 */
@AllArgsConstructor
@Getter
public class ActorMemoryLunar {
    RbfNetwork memoryMean;
    RbfNetwork memoryLogStd;
    TrainerParameters trainerParameters;

    public static ActorMemoryLunar create(AgentParameters ap, TrainerParameters tp, LunarParameters ep) {
        var memExp = RbfMemoryFactory.createMemoryManyCenters(ap, ep, tp.learningRateActor());
        var memStd = RbfMemoryFactory.createMemoryManyCenters(ap, ep, tp.learningRateActor());
        memStd.setWeights(ArrayCreatorUtil.createArrayWithSameDoubleNumber(memExp.nKernels(), ap.initWeightLogStd()));
        return new ActorMemoryLunar(memExp, memStd, tp);
    }
    /**
     * Fits the actor's memory using the provided training data.
     * @deprecated
     * This method is deprecated and should not be used due to performance issues.
     *
     * @param dataMean the training data for the mean
     * @param dataStd the training data for the standard deviation
     */
    @Deprecated(since = "slow and not recommended")
    public void fit(TrainData dataMean, TrainData dataStd) {
        memoryMean.fitFromErrors(dataMean, trainerParameters.nFits());
        memoryLogStd.fitFromErrors(dataStd, trainerParameters.nFits());
    }
/*
    *//**
     * Same but saves computation time using activations from other rbf
     *//*

    public void fitUsingActivationsOtherRbfMean(TrainData dataMean, TrainData dataStd, RbfNetwork other) {
        memoryMean.fitUsingActivationsOtherRbf(dataMean, agentParameters.nFits(),other);
        memoryLogStd.fitUsingActivationsOtherRbf(dataStd, agentParameters.nFits(),other);
    }*/

    /**
     * Returns the mean and standard deviation of the actor's output for the given state.
     *
     * @param state the input state
     * @return a MeanAndStd object containing the mean and standard deviation of the actor's output
     */

    public MeanAndStd actorMeanAndStd(StateLunar state) {
        var in = RadialBasisAdapter.asInput(state);
        return MeanAndStd.of(memoryMean.outPutListIn(in), Math.exp(memoryLogStd.outPutListIn(in)));
    }

    public MeanAndLogStd actorMeanAndLogStd(StateLunar state) {
        var in = RadialBasisAdapter.asInput(state);
        return MeanAndLogStd.of(memoryMean.outPutListIn(in), memoryLogStd.outPutListIn(in));
    }

}
