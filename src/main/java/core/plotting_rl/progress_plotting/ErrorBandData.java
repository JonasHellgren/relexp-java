package core.plotting_rl.progress_plotting;

import com.google.common.base.Preconditions;
import core.foundation.util.collections.ArrayCreator;
import core.foundation.util.collections.List2ArrayConverter;
import core.foundation.util.math.MovingAverage;

import java.util.List;

import static core.foundation.util.collections.MyListUtils.elementSubtraction;
import static core.foundation.util.collections.MyListUtils.everyItemAbsolute;


/**
 * Represents error band data, which is used to calculate error bounds for a given dataset.
 *
 * @param yData0 The original dataset.
 * @param nWindows The number of windows to divide the dataset into for filtering.
 */
public record ErrorBandData(List<Double> yData0, int nWindows) {

    public static ErrorBandData of(List<Double> yData0, int nWindows) {
        Preconditions.checkArgument(nWindows > 0, "nWindows should be > 0");
        Preconditions.checkArgument(yData0.size() >= nWindows, "yData0 should be equal exceed nWindows");
        return new ErrorBandData(yData0, nWindows);
    }

    int length() {
        return yData0.size();
    }

    List<Double> yDataFiltered() {
        return filter(yData0, length() / nWindows);
    }

    List<Double> errData0() {
        return elementSubtraction(yDataFiltered(), yData0);
    }

    List<Double> errData() {
        return everyItemAbsolute(errData0());
    }

    List<Double> errDataFiltered() {
        return filter(errData(), length() / (nWindows));
    }

    public double[] errDataFilteredAsArray() {
        return List2ArrayConverter.convertListToDoubleArr(errDataFiltered());
    }


    public double[] zeroErrDataAsArray() {
        return ArrayCreator.createArrayWithSameDoubleNumber(length(),0d);
    }

    public double[] yDataFilteredAsArray() {
        return List2ArrayConverter.convertListToDoubleArr(yDataFiltered());
    }


    public double[] yDataNotFilteredAsArray() {
        return List2ArrayConverter.convertListToDoubleArr(yData0);
    }


    public double[] xDataAsArray() {
        return ArrayCreator.createArrayFromStartAndEndWithNofItems(0d, length() - 1.0, length());
    }

    private static List<Double> filter(List<Double> inList, int lengthWindow) {
        MovingAverage movingAverage = new MovingAverage(lengthWindow, inList);
        return movingAverage.getFiltered();
    }


}

