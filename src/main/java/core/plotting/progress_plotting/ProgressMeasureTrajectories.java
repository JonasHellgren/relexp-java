package core.plotting.progress_plotting;


import core.foundation.util.collections.ListCreator;

import java.util.List;

/**
 * Represents a collection of progress measures for a training process.
 * This class provides a convenient way to store and manage various progress measures,
 * such as temporal differences, standard deviations, and gradient means.
 * A list represents data for a complete step.
 */

public record ProgressMeasureTrajectories(
        List<Double> tdList,
        List<Double> tdBestActionList,
        List<Double> tdListClipped,
        List<Double> stdList,
        List<Double> gradMeanList,
        List<Double> gradMeanListClipped
) {

    public static ProgressMeasureTrajectories empty() {
        return new ProgressMeasureTrajectories(
                ListCreator.emptyDouble(),
                ListCreator.emptyDouble(),
                ListCreator.emptyDouble(),
                ListCreator.emptyDouble(),
                ListCreator.emptyDouble(),
                ListCreator.emptyDouble());
    }

    public void addTd(double td) {
        tdList.add(td);
    }

    public void addTdBestAction(double td) {
        tdBestActionList.add(td);
    }

    public void addTdClipped(double td) {
        tdListClipped.add(td);
    }

    public void addStd(double std) {
        stdList.add(std);
    }

    public void addGradMean(double gradMean) {
        gradMeanList.add(gradMean);
    }

    public void addGradMeanClipped(double gradMean) {
        gradMeanListClipped.add(gradMean);
    }

}
