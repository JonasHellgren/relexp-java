package chapters.ch11.domain.trainer.multisteps;

import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.helper.RadialBasisAdapter;
import core.foundation.gadget.training.TrainData;
import core.foundation.gadget.training.TrainDataOld;
import lombok.AllArgsConstructor;

/***
 * The product of the gradient of the PDF and the advantage function is often clipped to prevent exploding gradients
  . Clipping can be done in two ways:
 1. Clipping the product: Clip the product of the gradient of the PDF and the advantage function together.
 2. Clipping the parts: Clip the gradient of the PDF and the advantage function separately before multiplying them.

  Clipping the parts (i.e., clipping the gradient of the PDF and the advantage function separately) is generally
  considered a better approach. Here's why:

  Clipping the product can lead to biased gradients, as the clipping operation can affect the direction of the gradient.
  Clipping the parts separately helps to prevent exploding gradients while preserving the direction of the gradient.
  In particular, clipping the advantage function can help to reduce the variance of the policy gradient estimate, while
  clipping the gradient of the PDF can help to prevent exploding gradients due to large policy updates.
 */

@AllArgsConstructor
public class TrainingDataCreator {

    public record DataContainer (TrainData dataCritic, TrainData dataMean, TrainData dataStd) {
        public static DataContainer empty() {
            return new DataContainer(
                    TrainData.empty(),
                    TrainData.empty(),
                    TrainData.empty());
        }
    }

    TrainerDependencies dependencies;

    public static TrainingDataCreator of(TrainerDependencies dependencies) {
       return new TrainingDataCreator(dependencies);
    }

    public DataContainer create(MultiStepResults msr, int step) {
        DataContainer data=DataContainer.empty();
        var agent = dependencies.agent();
        var ap = agent.getAgentParameters();
        double adv = ap.clipAdvantage(msr.advantageAtStep(step));
        var state = msr.stateAtStep(step);
        var grad = agent.gradientMeanAndLogStd(state, msr.actionAtStep(step));
        grad = grad.clip(ap.gradMeanMax(), ap.gradStdMax());
        var in = RadialBasisAdapter.asInput(state);
        data.dataMean.addListIn(in, grad.mean() * adv);
        data.dataStd.addListIn(in, grad.std() * adv);
        var inAtStep = RadialBasisAdapter.asInput(msr.stateAtStep(step));
        data.dataCritic.addListIn(inAtStep, adv);
        return data;
    }


}
