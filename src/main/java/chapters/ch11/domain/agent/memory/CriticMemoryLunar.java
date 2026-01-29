package chapters.ch11.domain.agent.memory;

import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.factory.RbfMemoryFactory;
import chapters.ch11.helper.RadialBasisAdapter;
import core.foundation.gadget.training.TrainData;
import core.nextlevelrl.radial_basis.RbfNetwork;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Represents the critic memory for the lunar lander agent.
 * This class is responsible for storing and updating the critic's RBF network.
 */

@AllArgsConstructor
@Getter
public class CriticMemoryLunar {

    RbfNetwork memory;
    AgentParameters agentParameters;

    public static CriticMemoryLunar zeroWeights(AgentParameters p, LunarParameters ep) {
        var mem = RbfMemoryFactory.createMemoryManyCenters(p,ep, p.learningRateCritic());
        return new CriticMemoryLunar(mem,p);
    }

    /**
     * Reads the output of the critic's memory for a given state.
     *
     * @param state the state to read the output for
     * @return the output of the critic's memory for the given state
     */
    public double read(StateLunar state) {
        var in = RadialBasisAdapter.asInput(state);
        return memory.outPutListIn(in);
    }

    /**
     * Fits the critic's memory to the given training data.
     *
     * @param data the training data to fit the memory to
     */
    public void fit(TrainData data) {
        memory.fitFromErrors(data, agentParameters.nEpochs());
    }

    public void fitFromError(TrainData data) {
        memory.fitFromErrors(data, agentParameters.nEpochs());
    }
}
