package chapters.ch13.animation;

import chapters.ch13.domain.searcher.path.Path;
import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.tree.TreeInfo;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.StateLane;
import core.animation.*;
import core.foundation.config.AnimationConfig;
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

    record EnvironmentTableData<S, A>(List<Object[][]> data) {
        private static <S, A> EnvironmentTableData<S, A> create(Path<S, A> path, Node<S, A> node, AnimationConfig cfg) {
            var isFail = false; //exp.stepReturn().isFail() ? "yes" : "no";

            Node<StateLane, ActionLane> nodeC = (Node<StateLane, ActionLane>) node;
            var state = nodeC.info().state();
            var data = Collections.singletonList(new Object[][]{
                    {"depth (max depth)", path.getNodes().indexOf(nodeC) + "(" + path.info().length() + ")"},
                    {"action", nodeC.info().action()},
                    {"steering angle (deg)", getRoundDeg(cfg, nodeC.info().action().getSteeringAngle())},
                    {"heading (deg)", getRoundDeg(cfg, state.headingAngle())},
                    {"(x,y) (m)", cfg.round(state.x())+"("+cfg.round(state.y())+")"},
            });
            return new EnvironmentTableData(data);
        }

        private static float getRoundDeg(AnimationConfig cfg, double v) {
            return cfg.round(UnitConverterUtil.convertRadiansToDegrees(v));
        }
    }

    static final int WIDTH = 350;
    static final int HEIGHT = 300;
    public static final int N_COL_ROWS_HEAT_MAP = 70;
    public static final int TABLE_HEIGHT_ENV = (int) (HEIGHT * 0.5);
    public static final int TABLE_HEIGHT_EPIS = (int) (HEIGHT * 0.15);
    static final int X_LOCATION_ENV = 20;
    static final int X_LOCATION_VAL = 400;
    static final int N_COLUMNS = 2;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 50.0, 49990.0),  //cuts
            List.of(250.0, 0.0, 250.0)   //animation time delays
    );

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;
    //SoundsPendulum sounds;
    //EnvironmentPendulum env;
    AnimationConfig cfg;

    public static AnimationLaneChange create(AnimationConfig cfg) {
        var asStep = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT_ENV, X_LOCATION_ENV);
        var asEpisode = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT_EPIS, X_LOCATION_VAL);
        return new AnimationLaneChange(
                AnimationKit.of(environmentGfx(asStep), asStep),
                AnimationKit.of(episodeGfx(asEpisode), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                cfg);
    }

    public static AnimationLaneChange empty() {
        return new AnimationLaneChange(
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
        addMidLines(lines,node);
        //addCar(lines);
        var tableData = EnvironmentTableData.create(path, node, cfg);
        var lineData = List.of(lines);
        var dto = GraphicsDto.dtoStep(lineData, tableData.data, animationDelay, false);
        kitStep.postAndSleep(dto);

    }

    private void addMidLines(List<LineSegment> lines, Node<S,A> node) {
        Node<StateLane, ActionLane> nodeC = nodeCasted(node);
        var p = LaneChangeParams.empty();

        double xPos=nodeC.info().state().x();
        double xMin=p.xmax();

        System.out.println("xPos = " + xPos);

        for (int i = 0; i < p.nMidLines(); i++) {
            double xLeft = p.posLeftSingleMidline(xPos, i);
            double xRigth = p.posRightSingleMidline(xPos, i);
            System.out.println(" xLeft = " + xLeft+" xRigth = "+xRigth);
            lines.add(LineSegment.line(xLeft, p.yMidLine(), xRigth, p.yMidLine(), p.midLineColor(), p.thiknessMidlines()));
        }



    }

    private static <S, A> Node<StateLane, ActionLane> nodeCasted(Node<S,A> node) {
        Node<StateLane, ActionLane> nodeC = (Node<StateLane, ActionLane>) node;
        return nodeC;
    }


    public void postEpisode(Pair<Integer, Integer> iter, TreeInfo<S, A> treeInfo) {
        if (isEmpty()) return;

        var angleList = ListCreatorUtil.createFromStartToEndWithNofItems(-40, 40, N_COL_ROWS_HEAT_MAP);
        var spdList = ListCreatorUtil.createFromStartToEndWithNofItems(-40, 40, N_COL_ROWS_HEAT_MAP);
        List<double[][]> grids = new ArrayList<>();
        double[][] vGrid = getData(angleList, spdList);
        grids.add(GridFactory.toSeries(vGrid, spdList, angleList));

        var tableData = Collections.singletonList(new Object[][]{
                {"Iteration (max iter):", iter.getFirst()+"("+iter.getSecond()+")"},
                {"Number of nodes:", treeInfo.numberOfNodes()}}
        );
        kitEpisode.postAndSleep(
                GraphicsDto.dtoEpisode(grids, tableData, (int) delayFunction.applyAsDouble(iter.getFirst())));
    }

    private double[][] getData(List<Double> angleList, List<Double> spdList) {
        double[][] data = new double[angleList.size()][spdList.size()];
        for (double a : angleList) {
            for (double spd : spdList) {
                int ai = angleList.indexOf(a);
                int spdi = spdList.indexOf(spd);
                double aRad = UnitConverterUtil.convertDegreesToRadians(a);
                double spdRad = UnitConverterUtil.convertDegreesToRadians(spd);
                data[ai][spdi] = 0;
            }
        }
        return data;
    }



    private static GfxComponentFactory environmentGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        var p = LaneChangeParams.empty();

        factory.addLineChart("",
                "x", Pair.create(0, (int) (2*p.carLenght())),
                "y", Pair.create(p.ymin(), p.ymax()));
        styleChart(factory.getLineCharts().get(0));
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        var plot = chart.getXYPlot();
        plot.setBackgroundPaint(LaneChangeParams.empty().colorBackground());
        //plot.getRangeAxis().setVisible(false); // disable Y axis
        plot.getDomainAxis().setVisible(false); // disable X axis
        plot.setDomainGridlinesVisible(false); // vertical grid lines
        plot.setRangeGridlinesVisible(false);  // horizontal grid lines
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
        plot.getRangeAxis().setLabel("");
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        var axisD = plot.getDomainAxis();
        axisD.setLabelFont(FONT_LABEL);
        var axisR = plot.getRangeAxis();
        axisR.setLabelFont(FONT_LABEL);
    }


}
