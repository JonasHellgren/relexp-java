package chapters.ch14.factory;

import chapters.ch14.domain.long_memory.LongMemory;
import chapters.ch14.environments.pong.PongSettings;
import chapters.ch14.environments.pong.StateLongPong;
import core.foundation.util.collections.ArrayCreatorUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryPlotData {

    public static double[][] getGridData(LongMemory<StateLongPong> memory,
                                         int length,
                                         PongSettings settings) {
        double[] xData = getXData(length, settings);
        double[] yData = getYData(length,settings);
        double[][] data = new double[yData.length][xData.length];
        for (int xi = 0; xi < xData.length; xi++) {
            for (int yi = 0; yi < yData.length; yi++) {
                double deltaX = xData[xi];
                double timeHit = yData[yi];
                var stateLong = StateLongPong.of(timeHit, deltaX);
                data[yi][xi] = memory.read(stateLong);
            }
        }
        return data;
    }

    public static double[] getXData(int length, PongSettings settings) {
        return ArrayCreatorUtil.createArrayFromStartAndEnd(length, 0, settings.xMax());
    }

    public static double[] getYData(int length, PongSettings settings) {
        return ArrayCreatorUtil.createArrayFromStartAndEnd(length, 0, settings.timeMaxHitBottom());
    }



}
