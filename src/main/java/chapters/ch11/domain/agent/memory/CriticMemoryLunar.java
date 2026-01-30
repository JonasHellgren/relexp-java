package chapters.ch11.domain.agent.memory;

import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.trainer.param.TrainerParameters;
import chapters.ch11.factory.RbfMemoryFactory;
import chapters.ch11.helper.RadialBasisAdapter;
import core.foundation.gadget.training.TrainDataErr;
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

    private RbfNetwork memory;
    private TrainerParameters trainerParameters;

    public static CriticMemoryLunar zeroWeights(AgentParameters ap, TrainerParameters tp, LunarParameters ep) {
        var mem = RbfMemoryFactory.createMemoryManyCenters(ap,ep, tp.learningRateCritic());
        return new CriticMemoryLunar(mem,tp);
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
    public void fit(TrainDataErr data) {
        memory.fitFromErrors(data, trainerParameters.nFits());
    }

}
