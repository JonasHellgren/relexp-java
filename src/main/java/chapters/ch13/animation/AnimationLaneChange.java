package chapters.ch13.animation;

import chapters.ch13.domain.searcher.path.Path;
import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.tree.TreeInfo;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.StateLane;
import core.animation.*;
import core.foundation.config.AnimationConfig;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.gadget.pos.PosXyDouble;
import core.foundation.util.collections.ListCreatorUtil;
import core.foundation.util.unit_converter.UnitConverterUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimationLaneChange<S, A> {

    public static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 12);
    public static final int N_NODESMAX_MARIGINAL = 1;

    record EnvironmentTableData<S, A>(List<Object[][]> data) {
        private static <S, A> EnvironmentTableData<S, A> create(Path<S, A> path, Node<S, A> node, AnimationConfig cfg) {
            var nodeC = nodeCasted(node);
            var state = nodeC.info().state();
            var data = Collections.singletonList(new Object[][]{
                    {"depth (max depth)", path.getNodes().indexOf(nodeC)+1 + "(" + path.info().length() + ")"},
                    {"action", nodeC.info().action()},
                    {"steering angle (deg)", getRoundDeg(cfg, nodeC.info().action().getSteeringAngle())},
                    {"heading (deg)", getRoundDeg(cfg, state.headingAngle())},
                    {"(x,y) (m)", "("+cfg.round(state.x())+"," + cfg.round(state.y()) + ")"},
            });
            return new EnvironmentTableData<>(data);
        }

        private static float getRoundDeg(AnimationConfig cfg, double v) {
            return cfg.round(UnitConverterUtil.convertRadiansToDegrees(v));
        }
    }

    static final int WIDTH = 350;
    static final int HEIGHT = 300;
    public static final int TABLE_HEIGHT_ENV = (int) (HEIGHT * 0.4);
    public static final int TABLE_HEIGHT_EPIS = (int) (HEIGHT * 0.15);
    static final int X_LOCATION_ENV = 20;
    static final int X_LOCATION_VAL = 400;
    static final int N_COLUMNS = 2;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 100.0, 110.0, 4990.0),  //cuts
            List.of(0.0, 500.0, 1.0, 250.0)   //animation time delays
    );

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;
    AnimationConfig cfg;

    public static <S, A> AnimationLaneChange<S, A> create(AnimationConfig cfg) {
        var asStep = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT_ENV, X_LOCATION_ENV);
        var asEpisode = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT_EPIS, X_LOCATION_VAL);
        return new AnimationLaneChange<>(
                AnimationKit.of(environmentGfx(asStep), asStep),
                AnimationKit.of(episodeGfx(asEpisode), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                cfg);
    }

    public static <S, A> AnimationLaneChange<S, A> empty() {
        return new AnimationLaneChange<>(
                AnimationKit.empty(),
                AnimationKit.empty(),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                AnimationConfig.defaults());
    }

    public boolean isEmpty() {
        return kitEpisode.isEmpty();
    }

    public void start() {
        if (isEmpty()) return;
        kitStep.start();
        kitEpisode.start();
    }

    public void postStep(Pair<Integer, Integer> iter, Path<S, A> path, Node<S, A> node) {
        if (isEmpty()) return;

        int animationDelay = (int) delayFunction.applyAsDouble(iter.getFirst());
        if (animationDelay == 0) return;

        List<LineSegment> lines = new ArrayList<>();
        addMidLines(lines, node);
        addCar(lines, node);
        var tableData = EnvironmentTableData.create(path, node, cfg);
        var lineData = List.of(lines);
        var dto = GraphicsDto.dtoStep(lineData, tableData.data, animationDelay, false);
        kitStep.postAndSleep(dto);

    }

    public void postEpisode(Pair<Integer, Integer> iter, TreeInfo<S, A> treeInfo, int maxDepth) {
        if (isEmpty()) return;

        var depthList = ListCreatorUtil.createFromZeroToNofItems(maxDepth);
        int nodesMax = treeInfo.numberOfNodesMaxAnyDepth();
        var nodeList = ListCreatorUtil.createFromZeroToNofItems(nodesMax + N_NODESMAX_MARIGINAL);
        List<double[][]> grids = new ArrayList<>();
        double[][] vGrid = getData(nodeList, depthList, treeInfo);
        grids.add(GridFactory.toSeries(vGrid, depthList, nodeList));
        var tableData = Collections.singletonList(new Object[][]{
                {"Iteration (max iter):", iter.getFirst() + "(" + iter.getSecond() + ")"},
                {"Number of nodes:", treeInfo.numberOfNodes()}}
        );
        kitEpisode.postAndSleep(
                GraphicsDto.dtoEpisode(grids, tableData, (int) delayFunction.applyAsDouble(iter.getFirst())));
    }

    private void addMidLines(List<LineSegment> lines, Node<S, A> node) {
        var nodeC = nodeCasted(node);
        var p = LaneChangeParams.empty();
        double xPos = nodeC.info().state().x();
        double y = nodeC.info().state().y();
        double angle = nodeC.info().state().headingAngle();
        var corners= p.carCorners(angle, y);

        for (int i = 0; i < p.nMidLines(); i++) {
            double xLeft = p.posLeftSingleMidline(xPos, i);
            double xRigth = p.posRightSingleMidline(xPos, i);
            if (p.isInsideRectangle(corners, PosXyDouble.of(xLeft, p.yMidLine())) ||
                    p.isInsideRectangle(corners, PosXyDouble.of(xRigth, p.yMidLine()))) continue;
            lines.add(LineSegment.line(xLeft, p.yMidLine(), xRigth, p.yMidLine(), p.midLineColor(), p.thiknessMidlines()));
        }
    }

    private void addCar(List<LineSegment> lines, Node<S, A> node) {
        var nodeC = nodeCasted(node);
        var p = LaneChangeParams.empty();
        double y = nodeC.info().state().y();
        double angle = nodeC.info().state().headingAngle();
        var c0 = p.carCorner(angle, y, 0);
        var c1 = p.carCorner(angle, y, 1);
        var c2 = p.carCorner(angle, y, 2);
        var c3 = p.carCorner(angle, y, 3);
        lines.add(LineSegment.line(c0.x(), c0.y(), c1.x(), c1.y(), p.carColor(), p.thiknessCarLine()));
        lines.add(LineSegment.line(c1.x(), c1.y(), c2.x(), c2.y(), p.carColor(), p.thiknessCarLine()));
        lines.add(LineSegment.line(c2.x(), c2.y(), c3.x(), c3.y(), p.carColor(), p.thiknessCarLine()));
        lines.add(LineSegment.line(c3.x(), c3.y(), c0.x(), c0.y(), p.carColor(), p.thiknessCarLine()));
    }

    private static <S, A> Node<StateLane, ActionLane> nodeCasted(Node<S, A> node) {
        Node<StateLane, ActionLane> nodeC = (Node<StateLane, ActionLane>) node;
        return nodeC;
    }

    private double[][] getData(List<Double> nodeList, List<Double> depthList, TreeInfo<S, A> treeInfo) {
        double minValue = treeInfo.minValue();
        double maxValue = treeInfo.maxValue();
        var scaler = ScalerLinear.of(minValue, maxValue, 0, 1);

        double[][] data = new double[nodeList.size()][depthList.size()];
        for (double depth : depthList) {
            int di = depthList.indexOf(depth);
            var nodes = treeInfo.nodesAtDepth(di);
            for (Node<S, A> node : nodes) {
                int ni = nodes.indexOf(node);
                data[ni][di] = scaler.calcOutDouble(node.info().value());
            }
        }
        return data;
    }

    private static GfxComponentFactory environmentGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        var p = LaneChangeParams.empty();

        factory.addLineChart("",
                "", Pair.create(0, p.xmax()),
                "y (m)", Pair.create(p.ymin(), p.ymax()));
        styleChart(factory.getLineCharts().get(0));
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        var plot = chart.getXYPlot();
        plot.setBackgroundPaint(LaneChangeParams.empty().colorBackground());
        plot.setDomainGridlinesVisible(false); // vertical grid lines
        plot.setRangeGridlinesVisible(false);  // horizontal grid lines
      //  plot.getRangeAxis().setVisible(false); // disable Y axis
        plot.getDomainAxis().setVisible(false); // disable X axis
    }

    private static GfxComponentFactory episodeGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addHeatMap("Tree");
        var heatmap = factory.getHeatMapCharts().get(0);
        styleMap(heatmap);
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static void styleMap(JFreeChart heatmap) {
        heatmap.getTitle().setFont(FONT_TITLE);
        var plot = heatmap.getXYPlot();
        plot.getDomainAxis().setLabel("Depth");
        plot.getRangeAxis().setLabel("Nodes");
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        var axisD = plot.getDomainAxis();
        axisD.setLabelFont(FONT_LABEL);
        var axisR = plot.getRangeAxis();
        axisR.setLabelFont(FONT_LABEL);
    }


}
