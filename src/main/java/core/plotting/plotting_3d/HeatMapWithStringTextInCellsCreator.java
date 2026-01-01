package core.plotting.plotting_3d;

import core.foundation.gadget.vector_algebra.ArrayMatrix;
import core.foundation.util.list_array.ArrayCreator;
import core.foundation.util.list_array.MatrixArrayCreator;
import core.foundation.util.list_array.MyArrayUtil;
import core.plotting.base.shared.PlotSettings;
import org.knowm.xchart.HeatMapChart;

import java.util.Arrays;

import static core.foundation.util.cond.Conditionals.executeIfTrue;


/**
 * Similar to HeatMapChartCreator, but with string text in cells
 * Not a fan of class extension. But here it is motivated.
 */

public class HeatMapWithStringTextInCellsCreator extends HeatMapChartCreator {

    String[][] stringData;

    public HeatMapWithStringTextInCellsCreator(
            PlotSettings settings, double[][] data, String[][] stringData, double[] xData0, double[] yData0) {
        super(settings, data, MyArrayUtil.doubleToInteger(xData0), MyArrayUtil.doubleToInteger(yData0));
        this.stringData = stringData;
    }

    public static HeatMapWithStringTextInCellsCreator ofStringData(
            PlotSettings settings, String[][] stringData, double[] xData0, double[] yData0) {
        double[][] data = MatrixArrayCreator.createAllZeroMatrix(stringData.length);
        return new HeatMapWithStringTextInCellsCreator(settings, data, stringData, xData0, yData0);
    }

    public static HeatMapWithStringTextInCellsCreator ofStringData(
            String[][] stringData, double[] xData0, double[] yData0) {
        return ofStringData(PlotSettings.stringTextInHeatMap(), stringData, xData0, yData0);
    }

    public static HeatMapWithStringTextInCellsCreator ofStringData(PlotSettings settings, String[][] stringData) {
        int nRows = stringData.length;
        int nCols = stringData[0].length;
        return ofStringData(settings,
                stringData,
                ArrayCreator.createArrayFromStartAndEnd(nCols, 0, nCols),
                ArrayCreator.createArrayFromStartAndEnd(nRows, 0, nRows));
    }

    /**
     * Create method is the major change w.r.t. HeatMapChartCreator
     */

    @Override
    public HeatMapChart create() {
        validate();
        var chart = super.createChart();
        super.addData(chart);
        executeIfTrue(getSettings().showDataValues(), () -> addStringDataText(chart));
        var styler=chart.getStyler();
        styler.setXAxisMin(-0.5d+getXData0()[0]);
        //styler.setXAxisMax(nCols() - 0.5d);
        styler.setXAxisMax(getXData0()[getXData0().length-1]+0.5);
        styler.setYAxisMin(-0.5d+getYData0()[0]);
        styler.setYAxisMax(getYData0()[getYData0().length-1]+0.5);
        return chart;
    }

    private void addStringDataText(HeatMapChart chart) {
        int[] xData = getXData(getXData0(), nCols());
        int[] yData = getYData(getYData0(), nRows());
        System.out.println("yData = " + Arrays.toString(yData));
        var scalers=Scalers.of(getSettings(), xData, yData,nCols(), nRows());
        for (int y = 0; y < nRows(); y++) {
            for (int x = 0; x < nCols(); x++) {
                String text = stringData[y][x];
                addTextToChart(chart, scalers.xScaler().calcOutDouble(x), scalers.yScaler().calcOutDouble(y), text);
            }
        }
    }

    @Override
    protected int nRows() {
        return ArrayMatrix.getDimensions(stringData).getFirst();
    }

    @Override
    protected int nCols() {
        return ArrayMatrix.getDimensions(stringData).getSecond();
    }


}
