package chapters.ch2.domain.fitting;

import lombok.Builder;
import lombok.With;
import java.util.List;

import static core.foundation.util.collections.ListCreatorUtil.createFromStartToEndWithNofItems;


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
        return createFromStartToEndWithNofItems(-range, range + margin, nPoints);
    }

}
