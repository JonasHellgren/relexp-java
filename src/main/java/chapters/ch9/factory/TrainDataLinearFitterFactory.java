package chapters.ch9.factory;

import core.foundation.gadget.training.TrainData;
import core.foundation.util.collections.ListCreatorUtil;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TrainDataLinearFitterFactory {

    public static TrainData getTrainData(double minX, double maxX)  {
        var data = TrainData.empty();
        var outList = List.of(
                4.5 + 1.0,
                4.2 + 1.0,
                5.2 + 2.0,
                5.4 + 2.0,
                5.0 + 3.0,
                5.6 + 3.0,
                5.0 + 4.0,
                5.2 + 4.0,
                5.3 + 5.0,
                5.6 + 5.0,
                5.0 + 6.0,
                5.2 + 6.0,
                5.0 + 7.0,
                5.1 + 7.0,
                4.7 + 8.0,
                4.9 + 8.0,
                4.9 + 9.0,
                4.7 + 9.0,
                5.2 + 10.0,
                5.0 + 10.0
        );

        var inList = ListCreatorUtil.createFromStartToEndWithNofItems(minX, maxX, outList.size());
        inList.stream().forEach(in ->
                data.addListIn(List.of(in), outList.get(inList.indexOf(in))));
        return data;
    }

}
