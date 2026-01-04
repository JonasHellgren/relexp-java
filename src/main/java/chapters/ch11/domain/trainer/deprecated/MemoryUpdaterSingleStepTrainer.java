package chapters.ch11.domain.trainer.deprecated;

import chapters.ch11.domain.trainer.core.ExperienceLunar;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.helper.RadialBasisAdapter;
import chapters.ch11.helper.ValueCalculatorLunar;
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
        var tp = dependencies.trainerParameters();
        var ap = agent.getAgentParameters();
        var dataMean = TrainDataOld.emptyFromErrors();
        var dataStd = TrainDataOld.emptyFromErrors();
        var data = TrainDataOld.emptyFromErrors();
        for (ExperienceLunar experience : experiences) {
            double tdError = ap.clipTdError(calculator.calcTemporalDifferenceError(experience));
            var state = experience.state();
            var grad = agent.gradientMeanAndLogStd(state, experience.action());
            grad = grad.clip(ap.gradMeanMax(), ap.gradStdMax());
            dataMean.clear();
            dataStd.clear();
            data.clear();
            var in = RadialBasisAdapter.asInput(state);
            dataMean.addInAndError(in, grad.mean() * tdError);
            dataStd.addInAndError(in, grad.std() * tdError);
            data.addInAndError(in, tdError);
            agent.fitActorUseCriticActivations(dataMean, dataStd);
            agent.fitCritic(data);
        }
    }

}
