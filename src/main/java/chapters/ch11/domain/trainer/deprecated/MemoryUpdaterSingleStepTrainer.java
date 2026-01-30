package chapters.ch11.domain.trainer.deprecated;

import chapters.ch11.domain.trainer.core.ExperienceLunar;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.helper.RadialBasisAdapter;
import chapters.ch11.helper.ValueCalculatorLunar;
import core.foundation.gadget.training.TrainData;
import core.foundation.gadget.training.TrainDataErr;
import core.foundation.gadget.training.TrainDataOld;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class MemoryUpdaterSingleStepTrainer {

    TrainerDependencies dependencies;
    ValueCalculatorLunar calculator;


    public static MemoryUpdaterSingleStepTrainer of(TrainerDependencies dependencies) {
        return new MemoryUpdaterSingleStepTrainer(dependencies, ValueCalculatorLunar.of(dependencies));
    }

    public void fit(List<ExperienceLunar> experiences) {
        var agent = dependencies.agent();
        var ap = agent.getAgentParameters();
        var dataMean = TrainDataErr.empty();
        var dataStd = TrainDataErr.empty();
        var data = TrainDataErr.empty();
        for (ExperienceLunar experience : experiences) {
            double tdError = ap.clipTdError(calculator.calcTemporalDifferenceError(experience));
            var state = experience.state();
            var grad = agent.gradientMeanAndLogStd(state, experience.action());
            grad = grad.clip(ap.gradMeanMax(), ap.gradStdMax());
            dataMean.clear();
            dataStd.clear();
            data.clear();
            var in = RadialBasisAdapter.asInput(state);
            dataMean.addListIn(in, grad.mean() * tdError);
            dataStd.addListIn(in, grad.std() * tdError);
            data.addListIn(in, tdError);
            agent.fitActor(dataMean, dataStd);
            agent.fitCritic(data);
        }
    }

}
