package chapters.ch11.domain.agent.memory;


import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.factory.RbfMemoryFactory;
import chapters.ch11.helper.RadialBasisAdapter;
import chapters.ch9.radial_basis.RbfNetwork;
import core.foundation.gadget.math.MeanAndLogStd;
import core.foundation.gadget.math.MeanAndStd;
import core.foundation.gadget.training.TrainData;
import core.foundation.util.collections.ArrayCreator;
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
    AgentParameters agentParameters;

    public static ActorMemoryLunar create(AgentParameters p, LunarParameters ep) {
        var memExp = RbfMemoryFactory.createMemoryManyCenters(p, ep, p.learningRateActor());
        var memStd = RbfMemoryFactory.createMemoryManyCenters(p, ep, p.learningRateActor());
        memStd.setWeights(ArrayCreator.createArrayWithSameDoubleNumber(memExp.nKernels(), p.initWeightLogStd()));
        return new ActorMemoryLunar(memExp, memStd, p);
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
        int batchSize = Math.min(dataMean.nSamples(), agentParameters.batchSize());
        memoryMean.fit(dataMean, agentParameters.nEpochs(),batchSize);
        memoryLogStd.fit(dataStd, agentParameters.nEpochs(),batchSize);
    }

    /**
     * Same but saves computation time using activations from other rbf
     */

    public void fitUsingActivationsOtherRbfMean(TrainData dataMean, TrainData dataStd, RbfNetwork other) {
        int batchSize = Math.min(dataMean.nSamples(), agentParameters.batchSize());
        memoryMean.fitUsingActivationsOtherRbf(dataMean, agentParameters.nEpochs(),batchSize,other);
        memoryLogStd.fitUsingActivationsOtherRbf(dataStd, agentParameters.nEpochs(),batchSize,other);
    }

    /**
     * Returns the mean and standard deviation of the actor's output for the given state.
     *
     * @param state the input state
     * @return a MeanAndStd object containing the mean and standard deviation of the actor's output
     */

    public MeanAndStd actorMeanAndStd(StateLunar state) {
        var in = RadialBasisAdapter.asInput(state);
        return MeanAndStd.of(memoryMean.outPut(in), Math.exp(memoryLogStd.outPut(in)));
    }

    public MeanAndLogStd actorMeanAndLogStd(StateLunar state) {
        var in = RadialBasisAdapter.asInput(state);
        return MeanAndLogStd.of(memoryMean.outPut(in), memoryLogStd.outPut(in));
    }

}
