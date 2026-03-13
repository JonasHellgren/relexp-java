package chapters.ch10.animation.cannon;

import chapters.ch10.cannon.domain.agent.MemoryCannon;
import chapters.ch10.cannon.domain.trainer.ExperienceCannon;
import core.animation.*;
import core.foundation.config.AnimationConfig;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.collections.MyMatrixArrayUtil;
import core.foundation.util.unit_converter.UnitConverterUtil;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;
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

    static final int WIDTH = 350;
    static final int HEIGHT = 200;
    public static final int TABLE_HEIGHT = (int) (HEIGHT * 0.5);
    static final int X_LOCATION_ENV = 20;
    static final int X_LOCATION_VAL = 400;
    static final int N_COLUMNS = 2;
    static final int DIST_REF = 800;
    static final int DIST_DIFF = 30;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 20.0, 990.0),  //cuts
            List.of(1000.0, 1.0, 1000.0)   //animation time delays
    );

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;
    SoundsCannon sounds;
    CannonParams params;
    AnimationConfig cfg;

    public static AnimationCannon create(AnimationConfig cfg) {
        var asStep = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT, X_LOCATION_ENV);
        var asEpisode = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT, X_LOCATION_VAL);
        return new AnimationCannon(
                AnimationKit.of(environmentGfx(asStep), asStep),
                AnimationKit.of(episodeGfx(asEpisode), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                SoundsCannon.create(),
                CannonParams.create(),
                cfg);
    }

    public static AnimationCannon empty() {
        return new AnimationCannon(
                AnimationKit.empty(),
                AnimationKit.empty(),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                SoundsCannon.create(),
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
        addTargetLines(lines);
        addFireDots(lines, exp);
        sounds.playFire();
        var tableData = TableData.create(ei, eiMax, exp, cfg);
        postCommon(ei, eiMax, exp, lines, tableData);
    }

    public void postHit(int ei, int eiMax, List<ExperienceCannon> experiences) {
        var exp = experiences.get(0);
        List<LineSegment> lines = new ArrayList<>();
        addCannonAndFire(lines, exp);
        if (isHit(exp)) {
            sounds.playHit();
            addHitFire(lines, exp, params.radiusFireDotsHitLarge());
        } else {
            sounds.playSplat();
            addTargetLines(lines);
            addHitFire(lines, exp, params.radiusFireDotsHitSmall());
        }

        var tableData = TableData.create(ei, eiMax, exp, cfg);
        postCommon(ei, eiMax, exp, lines, tableData);
    }

    public void postEpisode(MemoryCannon memory, int ei, Pair<Double,Double> baseReturn, GradientMeanAndLogStd grad) {
        if (isEmpty()) return;
        int nRows = 1;
        int nCols = 2;
        double[][] vGrid = MyMatrixArrayUtil.emptyMatrix(nRows, nCols);
        var scalerMean = ScalerLinear.of(0,90, 0.0, 1.0);
        var scalerStd = ScalerLinear.of(0, 10, 0.0, 1.0);
        double expAngleDeg = UnitConverterUtil.convertRadiansToDegrees(memory.mean());
        double stdAngleDeg = UnitConverterUtil.convertRadiansToDegrees(memory.std());
        vGrid[0][0] = scalerMean.calcOutDouble(expAngleDeg);
        vGrid[0][1] = scalerStd.calcOutDouble(stdAngleDeg);
        List<double[][]> grids = new ArrayList<>();
        grids.add(GridFactory.toSeries(vGrid));
        var tableData = Collections.singletonList(new Object[][]{
                {"("+"m,d"+")", "(" + cfg.round(expAngleDeg) + "," + cfg.round(stdAngleDeg) + ")"},
                {"base", cfg.round(baseReturn.getFirst())},
                {"base-return", cfg.round(baseReturn.getFirst()-baseReturn.getSecond())},
                {"grad log", "(" + cfg.round(grad.mean()) + "," + cfg.round(grad.std()) + ")"},
        });
        kitEpisode.postAndSleep(GraphicsDto.dtoEpisode(grids, tableData, (int) delayFunction.applyAsDouble(ei)));
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


    private void addHitFire(List<LineSegment> lines, ExperienceCannon exp, int radiusFireDotsHit) {
        var p = params;
        var dist = exp.stepReturn().distance();
        for (int i = 0; i < p.nFireDots(); i++) {
            var xyPos = p.randomPosInCircle(Pair.create((int) dist, 0), radiusFireDotsHit);
            lines.add(LineSegment.circleCommon(
                    xyPos.getFirst(), xyPos.getSecond(),
                    p.randomColor(p.targetHitColors()), p.radiusFireDot()));
        }
    }

    private void addTargetLines(List<LineSegment> lines) {
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
            var hitText = isHit(exp) ? "yes" : "no";
            var data = Collections.singletonList(new Object[][]{
                    {"episode", ei + "(" + eiMax + ")"},
                    {"action (angle in deg)", cfg.round(UnitConverterUtil.convertRadiansToDegrees(exp.action()))},
                    {"distance to hit (m)", cfg.round(exp.stepReturn().distance())},
                    {"reward, is hit?", cfg.round(exp.reward())+", "+hitText},
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
        int animationDelay = (int) delayFunction.applyAsDouble(ei);
        var dto = GraphicsDto.dtoStep(lineData, tableData.data, animationDelay, false);
        kitStep.postAndSleep(dto);
    }


    private static GfxComponentFactory environmentGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addLineChart("",
                "x", Pair.create(-10, 900),
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



}
