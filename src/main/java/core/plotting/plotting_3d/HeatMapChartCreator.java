package core.plotting.plotting_3d;

import com.google.common.base.Preconditions;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.gadget.vector_algebra.ArrayMatrix;
import core.foundation.util.cond.Conditionals;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.foundation.util.list_array.ArrayCreator;
import core.foundation.util.list_array.MyArrayUtil;
import core.plotting.base.shared.PlotSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import org.knowm.xchart.AnnotationText;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapChartBuilder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.stream.IntStream;

import static core.foundation.util.cond.Conditionals.executeIfTrue;
import static core.foundation.util.formatting.NumberFormatterUtil.getRoundedNumberAsString;
import static core.foundation.util.list_array.MyMatrixArrayUtils.findMax;
import static core.foundation.util.list_array.MyMatrixArrayUtils.findMin;

/**
 * This class is used to create a HeatMapChart.
 * It uses the Lombok @Builder annotation to generate a builder for the Settings class.
 * The Settings class is a record that holds the configuration for the HeatMapChart.
 * The HeatMapChartCreator class has a private constructor and is created using the ofSettings() or defaultSettings() methods.
 * The create() method is used to create the HeatMapChart.
 * The addData() method is used to add the data to the HeatMapChart.
 * The addCellText() method is used to add the cell text to the HeatMapChart.
 * The addTextToChart() method is used to add the text to the HeatMapChart.
 * <p>
 * chart.addSeries only works with integer arrays, hence data must be converted to integer arrays.
 * Therefore, data shall be wide, for example is data between 0 and 1 not appropriate
 */

@AllArgsConstructor
@Log
@Getter
public class HeatMapChartCreator {
    static final int MANY_ROWS = 20;
    static final int MANY_COLS = 20;

    private final PlotSettings settings;
    private final double[][] data;  //looked up by data[y][x]
    private final int[] xData0;
    private final int[] yData0;

    public static HeatMapChartCreator defaultSettings(double[][] data) {
        return new HeatMapChartCreator(PlotSettings.ofDefaults(), data, null, null);
    }

    public static HeatMapChartCreator of(PlotSettings settings, double[][] data) {
        return new HeatMapChartCreator(settings, data, null, null);
    }

    public static HeatMapChartCreator of(PlotSettings settings, double[][] data, double[] xData, double[] yData) {
        return new HeatMapChartCreator(
                settings,
                data,
                MyArrayUtil.doubleToInteger(xData),
                MyArrayUtil.doubleToInteger(yData));
    }

    /**
     * Creates a new HeatMapChart.
     *
     * @return The HeatMapChart.
     */
    public HeatMapChart create() {
        validate();
        var chart = createChart();
        addData(chart);
        executeIfTrue(settings.showDataValues(), () -> addCellText(chart));
        return chart;
    }

    protected void validate() {
        Preconditions.checkArgument(nRows() > 0, "data must have at least one row");
        Preconditions.checkArgument(nCols() > 0, "data must have at least one column");
        Conditionals.executeIfTrue(settings.showAxisTicks() && manyRowsOrColumns(), () ->
                log.warning("To many rows or columns for axis ticks "));
        Conditionals.executeIfTrue(settings.showDataValues() && manyRowsOrColumns(), () ->
                log.warning("To many rows or columns for showing data values "));
    }

    private boolean manyRowsOrColumns() {
        return nRows() > MANY_ROWS || nCols() > MANY_COLS;
    }

    protected HeatMapChart createChart() {
        var chart = new HeatMapChartBuilder()
                .title(settings.title())
                .xAxisTitle(settings.xAxisLabel()).yAxisTitle(settings.yAxisLabel())
                .width(settings.width()).height(settings.height())
                .build();

       /* double minValue = Double.parseDouble(formatterTwoDigits.format(findMin(data)));
        double maxValue = Double.parseDouble(formatterTwoDigits.format(findMax(data)));

        double minValue = NumberFormatterUtil.roundTo2Decimals(findMin(data));
        double maxValue = NumberFormatterUtil.roundTo2Decimals(findMax(data));
*/

        int n = settings.nDigitsAnnotationText();
        double minValue = NumberFormatterUtil.roundToNDecimals(findMin(data), n);
        double maxValue = NumberFormatterUtil.roundToNDecimals(findMax(data),n);
        var styler = chart.getStyler();
        styler.setChartTitleVisible(true).setLegendVisible(settings.showLegend());
        styler.setPlotGridLinesVisible(settings.showGridLines());
        styler.setAxisTicksVisible(settings.showAxisTicks());
        styler.setAxisTitleFont(settings.axisTitleFont());
        styler.setAxisTickLabelsFont(settings.axisTicksFont());
        styler.setAnnotationTextFont(settings.axisTicksFont());
        styler.setLegendFont(settings.axisTicksFont());
        styler.setChartTitleFont(settings.axisTitleFont());

        styler.setMin(minValue).setMax(maxValue).setRangeColors(settings.colorRange());
        styler.setAnnotationTextFontColor(settings.annotationTextFontColor());
        styler.setAnnotationTextFont(settings.annotationTextFont());
        styler.setxAxisTickLabelsFormattingFunction(value -> getFormattedAsString(value));
        styler.setyAxisTickLabelsFormattingFunction(value -> getFormattedAsString(value));
        styler.setChartBackgroundColor(Color.WHITE);
        //below line are to get correct x and y range in plot
        double[] xDataExt = getDataExt(getXData(xData0, nCols()));
        styler.setXAxisMin(xDataExt[0]).setXAxisMax(xDataExt[xDataExt.length-1]);
        double[] yDataExt = getDataExt(getYData(yData0, nRows()));
        styler.setYAxisMin(yDataExt[0]).setYAxisMax(yDataExt[yDataExt.length-1]);
        return chart;
    }

    private static double[] getDataExt(int[] axisData) {
        int len=axisData.length;
        int max = axisData[axisData.length - 1];
        int min = axisData[0];
        double step= (double) (max - min) /(len-1);
        return ArrayCreator.createArrayInRange(min, step, max+step);
    }

    private String getFormattedAsString(Double value) {
        DecimalFormat df = new DecimalFormat(settings.axisTicksDecimalFormat());
        return df.format(value);
    }

    protected void addData(HeatMapChart chart) {
        double[][] dataRot = ArrayMatrix.transposeMatrix(data);
        int[][] dataRotInt = ArrayMatrix.doubleToInt(dataRot);
        int[] xData = getXData(xData0, nCols());
        int[] yData = getYData(yData0, nRows());
        chart.addSeries("seriesname", xData, yData, dataRotInt);
    }

    /**
     * The scalers are needed to place the text in the right place, mid of the cells
     */

    public record Scalers(ScalerLinear xScaler, ScalerLinear yScaler) {
        public static Scalers of(PlotSettings settings, int[] xData, int[] yData,int nCols, int nRows) {
            double minMargin = settings.minCellMargin();
            double maxMargin = settings.maxCellMargin();
            var xScaler = new ScalerLinear(minMargin, nCols - 1 + maxMargin, xData[0], xData[xData.length - 1]);
            var yScaler = new ScalerLinear(minMargin, nRows - 1 + maxMargin, yData[0], yData[yData.length - 1]);
            return new Scalers(xScaler, yScaler);
        }

    }

    private void addCellText(HeatMapChart chart) {
        int[] xData = getXData(xData0, nCols());
        int[] yData = getYData(yData0, nRows());
        var scalers=Scalers.of(settings, xData, yData,nCols(), nRows());
        for (int y = 0; y < nRows(); y++) {
            for (int x = 0; x < nCols(); x++) {
                String text = getRoundedNumberAsString(data[y][x], settings.nDigitsAnnotationText());
                addTextToChart(
                        chart,
                        scalers.xScaler.calcOutDouble(x),
                        scalers.yScaler.calcOutDouble(y),
                        text);
            }
        }
    }

    protected void addTextToChart(HeatMapChart chart, double xPos, double yPos, String text) {
        AnnotationText annotation = new AnnotationText(text, xPos, yPos, false);
        chart.addAnnotation(annotation);
    }

    protected int[] getYData(int[] yData0, int nRows) {
        return (yData0 != null) ? yData0 : IntStream.rangeClosed(0, nRows - 1).toArray();
    }

    protected int[] getXData(int[] xData0, int nCols) {
        return (xData0 != null) ? xData0 : IntStream.rangeClosed(0, nCols - 1).toArray();
    }

    protected int nRows() {
        return ArrayMatrix.getDimensions(data).getFirst();
    }

    protected int nCols() {
        return ArrayMatrix.getDimensions(data).getSecond();
    }

}
