package chapters.ch2.factory;

import chapters.ch2.domain.fitting.FittingParameters;
import chapters.ch2.domain.parameter_fitting.LearningRateFittingResults;
import chapters.ch2.impl.parameter_fitting.FitterSingleParameter;
import core.foundation.gadget.training.TrainDataInOut;
import core.foundation.util.collections.MyListUtils;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TrainingResultsGenerator {
    public static final double OUTPUT_TARGET = 1.0;
    public static final double START_W = 0.0d;

    public static LearningRateFittingResults getTrainingResults(FittingParameters par0, List<Double> learningRates) {
        var trainingResults = LearningRateFittingResults.empty();
        for (double learningRate : learningRates) {
            var par1 = par0.withLearningRate(learningRate);
            var fitter = FitterSingleParameter.of(par1);
            var data = TrainDataInOut.empty();
            var outputs0 = getOutputs(data, par0, fitter);
            var outputs = MyListUtils.merge(List.of(START_W),outputs0);
            trainingResults.add(learningRate, outputs);
        }
        return trainingResults;
    }

    private static List<Double> getOutputs(TrainDataInOut data, FittingParameters par0, FitterSingleParameter fitter) {
        data.add(List.of(0d), OUTPUT_TARGET);
        List<Double> results = new ArrayList<>();
        for (int i = 0; i < par0.nofIterations(); i++) {
            fitter.fit(data);
            results.add(fitter.read(0d));
        }
        return results;
    }


}
