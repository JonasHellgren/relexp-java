package chapters.ch5.factory;

import core.foundation.util.collections.ArrayCreator;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public record GridProperties(
        int nCols,
        int startCol,
        int startRow,
        int nRows
) {


    public  double[] getXData() {
        return ArrayCreator.createArrayFromStartAndEnd(nCols, startCol,startCol+ nCols- 1);
    }

    public  double[] getYData() {
        return ArrayCreator.createArrayFromStartAndEnd(nRows, 0, nRows- 1);
    }

    public  String[][] getEmptyValueData() {
        return new String[nRows][nCols];
    }

    public int endCol() {
        return startCol + nCols;
    }


}
