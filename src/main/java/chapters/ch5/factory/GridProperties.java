package chapters.ch5.factory;

import core.foundation.util.collections.ArrayCreatorUtil;
import lombok.Builder;

@Builder
public record GridProperties(
        int nCols,
        int startCol,
        int startRow,
        int nRows
) {


    public  double[] getXData() {
        return ArrayCreatorUtil.createArrayFromStartAndEnd(nCols, startCol,startCol+ nCols- 1);
    }

    public  double[] getYData() {
        return ArrayCreatorUtil.createArrayFromStartAndEnd(nRows, 0, nRows- 1);
    }

    public  String[][] getEmptyValueData() {
        return new String[nRows][nCols];
    }

    public int endCol() {
        return startCol + nCols;
    }


}
