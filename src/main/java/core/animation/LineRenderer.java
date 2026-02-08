package core.animation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.plot.XYPlot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LineRenderer {

    private final XYPlot plot;
    private final List<XYLineAnnotation> annotations;

    public static LineRenderer of(JFreeChart chart) {
        return LineRenderer.of(chart.getXYPlot());
    }
    public static LineRenderer of(XYPlot plot) {
        return new LineRenderer(plot, new ArrayList<>());
    }

    public void setLines(List<LineSegment> lines) {
        clearOldLines();
        addNewLines(lines);
    }

    private void addNewLines(List<LineSegment> lines) {
        for (var l : lines) {
            var ann = getAnnotation(l);
            plot.addAnnotation(ann);
            annotations.add(ann);
        }
    }

    private static XYLineAnnotation getAnnotation(LineSegment l) {
        var stroke = new BasicStroke(l.thickness(), l.endRounding(), l.joining());
        return new XYLineAnnotation(
                l.x1(), l.y1(),
                l.x2(), l.y2(),
                stroke, l.color()
        );
    }

    private void clearOldLines() {
        for (var a : annotations) {
            plot.removeAnnotation(a);
        }
        annotations.clear();
    }

}
