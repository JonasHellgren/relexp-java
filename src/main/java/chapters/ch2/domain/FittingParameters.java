package chapters.ch2.domain;

import core.foundation.util.list.ListCreator;
import lombok.Builder;
import lombok.With;
import java.util.List;

@Builder
@With
public record FittingParameters(
        double learningRate,
        int nofIterations,
        double defaultMemoryValue,
        double range,
        double deltaX,
        double margin   //just to make the chart look nice
) {

    public  List<Double> getXList(int nPoints) {
        double range = range();
        double margin = margin();
        return ListCreator.createFromStartToEndWithNofItems(-range, range + margin, nPoints);
    }

}
