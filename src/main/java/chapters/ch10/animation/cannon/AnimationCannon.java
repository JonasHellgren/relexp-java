package chapters.ch10.animation.cannon;

import chapters.ch10.animation.bandit.SoundsBandit;
import chapters.ch10.bandit.domain.agent.MemoryBandit;
import chapters.ch10.bandit.domain.trainer.ExperienceBandit;
import chapters.ch10.cannon.domain.trainer.ExperienceCannon;
import core.animation.*;
import core.foundation.config.AnimationConfig;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.collections.MyMatrixArrayUtil;
import core.foundation.util.cond.ConditionalsUtil;
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
public class AnimationCannon {

    static final int HEIGHT_ENV = 200;
    public static final int TABLE_HEIGHT = (int) (HEIGHT_ENV * 0.75);
    static final int HEIGHT_VAL = 200;
    static final int WIDTH = 300;
    static final int X_LOCATION_ENV = 100;
    static final int X_LOCATION_VAL = 500;
    static final int N_COLUMNS = 2;
    static final int Z_MAX = 5;
    static final int DIST_REF = 800;
    static final int DIST_DIFF = 30;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 10.0, 990.0),  //cuts
            List.of(500.0, 1.0, 500.0)   //animation time delays
    );

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;
    SoundsBandit sounds;
    CannonParams params;
    AnimationConfig cfg;

    public static AnimationCannon create(AnimationConfig cfg) {
        var asStep = AnimationSettings.of(cfg, WIDTH, HEIGHT_ENV, TABLE_HEIGHT, X_LOCATION_ENV);
        var asEpisode = AnimationSettings.of(cfg, WIDTH, HEIGHT_VAL, TABLE_HEIGHT, X_LOCATION_VAL);
        return new AnimationCannon(
                AnimationKit.of(environmentGfx(asStep), asStep),
                AnimationKit.of(episodeGfx(asEpisode), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                SoundsBandit.create(),
                CannonParams.create(),
                cfg);
    }

    public static AnimationCannon empty() {
        return new AnimationCannon(
                AnimationKit.empty(),
                AnimationKit.empty(),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                SoundsBandit.create(),
                CannonParams.create(),
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

    public void postFire(int ei, int eiMax, List<ExperienceCannon> experiences) {
        var exp = experiences.get(0);
        List<LineSegment> lines = new ArrayList<>();
        addCannonAndFire(lines, exp);
        addTargetLines(lines, exp);
        addFireDots(lines, exp);
        var tableData = TableData.create(ei, eiMax, exp, cfg);
        postCommon(ei, eiMax, exp, lines, tableData);
        sounds.playCoin();
    }

    public void postHit(int ei, int eiMax, List<ExperienceCannon> experiences) {
        var exp = experiences.get(0);
        List<LineSegment> lines = new ArrayList<>();
        addCannonAndFire(lines, exp);
        addHitFire(lines, exp);
        ConditionalsUtil.executeIfFalse(isHit(exp), () -> addTargetLines(lines, exp));
        var tableData = TableData.create(ei, eiMax, exp, cfg);
        postCommon(ei, eiMax, exp, lines, tableData);
        ConditionalsUtil.executeIfTrue(isHit(exp), () -> sounds.playCoin());
    }

    private void addCannonAndFire(List<LineSegment> lines, ExperienceCannon exp) {
        var p = params;
        var angle = exp.action();
        lines.add(LineSegment.line(
                p.cannonWestXpos(), p.cannonWestXpos(),
                p.cannonEastXPos(angle), p.cannonEastYPos(angle),
                p.cannonColor(), p.widthCannon()));
    }


    private void addFireDots(List<LineSegment> lines, ExperienceCannon exp) {
        var angle = exp.action();
        var p = params;
        for (int i = 0; i < p.nFireDots(); i++) {
            var xyPos = p.randomPosInCircle(p.centerCannonFire(angle), p.radiusFireDotsCannon());
            lines.add(LineSegment.circleCommon(
                    xyPos.getFirst(), xyPos.getSecond(),
                    p.randomColor(p.fireColors()), p.radiusFireDot()));
        }
    }


    private void addHitFire(List<LineSegment> lines, ExperienceCannon exp) {
        var p = params;
        var dist = exp.stepReturn().distance();
        for (int i = 0; i < p.nFireDots(); i++) {
            var xyPos = p.randomPosInCircle(Pair.create((int) dist, 0), p.radiusFireDotsHit());
            lines.add(LineSegment.circleCommon(
                    xyPos.getFirst(), xyPos.getSecond(),
                    p.randomColor(p.targetHitColors()), p.radiusFireDot()));
        }
    }

    private void addTargetLines(List<LineSegment> lines, ExperienceCannon exp) {
        var dist = params.distTarget();
        int x1 = params.targetLeft(dist);
        int x2 = params.targetRight(dist);
        int h1 = params.heightTarget();
        int h2 = 0;
        lines.add(LineSegment.black(x1, h1, x2, h1));
        lines.add(LineSegment.black(x2, h1, x2, h2));
        lines.add(LineSegment.black(x1, h2, x2, h2));
        lines.add(LineSegment.black(x1, h1, x1, h2));
    }

    record TableData(List<Object[][]> data) {
        private static TableData create(int ei, int eiMax, ExperienceCannon exp, AnimationConfig cfg) {
            var data = Collections.singletonList(new Object[][]{
                    {"episode", ei + "(" + eiMax + ")"},
                    {"action (angle in deg)", cfg.round(UnitConverterUtil.convertRadiansToDegrees(exp.action()))},
                    {"distance to hit (m)", cfg.round(exp.stepReturn().distance())},
                    {"reward", cfg.round(exp.reward())},
                    {"is hit?", isHit(exp) ? "yes" : "no"},
            });
            return new TableData(data);
        }

    }

    private static boolean isHit(ExperienceCannon exp) {
        return Math.abs((exp.stepReturn().distance() - DIST_REF)) <= DIST_DIFF;
    }

    private void postCommon(int ei, int eiMax, ExperienceCannon exp, List<LineSegment> lines, TableData tableData) {
        if (isEmpty()) return;

        var lineData = List.of(lines);
        //var tableData = setTabledata(ei, eiMax, exp);
        int animationDelay = (int) delayFunction.applyAsDouble(ei);
        var dto = GraphicsDto.dtoStep(lineData, tableData.data, animationDelay, false);
        kitStep.postAndSleep(dto);
    }


    public void postEpisode(MemoryBandit memory, int ei, double returnAtT, double[] gradLog, double[] probArray) {
        if (isEmpty()) return;
        int nRows = 1;
        int nCols = 2;
        double[][] vGrid = MyMatrixArrayUtil.emptyMatrix(nRows, nCols);
        var z = memory.getMemoryParameters();
        var scaler = ScalerLinear.of(-Z_MAX, Z_MAX, 0.0, 1.0);
        vGrid[0][0] = scaler.calcOutDouble(z[0]);
        vGrid[0][1] = scaler.calcOutDouble(z[1]);
        List<double[][]> grids = new ArrayList<>();
        grids.add(GridFactory.toSeries(vGrid));
        var tableData = Collections.singletonList(new Object[][]{
                {"z", "(" + cfg.round(z[0]) + "," + cfg.round(z[1]) + ")"},
                {"return", cfg.round(returnAtT)},
                {"grad log", "(" + cfg.round(gradLog[0]) + "," + cfg.round(gradLog[1]) + ")"},
                {"probability (L,R)", "(" + cfg.round(probArray[0]) + "," + cfg.round(probArray[1]) + ")"},
        });
        kitEpisode.postAndSleep(GraphicsDto.dtoEpisode(grids, tableData, (int) delayFunction.applyAsDouble(ei)));
    }


    private static GfxComponentFactory environmentGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addLineChart("",
                "x", Pair.create(-10, 1000),
                "y", Pair.create(0, 200));
        styleChart(factory.getLineCharts().get(0));
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        var plot = chart.getXYPlot();
        plot.setBackgroundPaint(CannonParams.create().colorBackground());
        //    plot.getDomainAxis().setVisible(false); // disable X axis
        plot.getRangeAxis().setVisible(false);  // disable Y axis
        plot.setDomainGridlinesVisible(false); // vertical grid lines
        plot.setRangeGridlinesVisible(false);  // horizontal grid lines
    }

    private static GfxComponentFactory episodeGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addHeatMap("Agent memory");
        var heatmap = factory.getHeatMapCharts().get(0);
        heatmap.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));
        var plot = heatmap.getXYPlot();
        plot.getDomainAxis().setVisible(false); // disable X axis
        plot.getRangeAxis().setVisible(false);  // disable Y axis
        factory.addTable(N_COLUMNS, false);
        return factory;
    }


    private static void addBanditLines(List<LineSegment> lines, boolean isStep) {
        //var p = BanditParams.create();
        // Machine body
        //lines.add(LineSegment.black(p.left(), p.top(), p.right(), p.top()));       // top

    }


}
