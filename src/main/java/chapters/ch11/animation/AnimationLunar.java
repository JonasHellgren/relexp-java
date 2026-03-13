package chapters.ch11.animation;

import chapters.ch10.cannon.domain.agent.MemoryCannon;
import chapters.ch10.cannon.domain.trainer.ExperienceCannon;
import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.trainer.multisteps.MultiStepResult;
import core.animation.*;
import core.foundation.config.AnimationConfig;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.collections.MyMatrixArrayUtil;
import core.foundation.util.cond.ConditionalsUtil;
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
public class AnimationLunar {


    record TableData(List<Object[][]> data) {
        private static TableData create(int ei,
                                        int eiMax,
                                        MultiStepResult msr,
                                        EnvironmentLunar env,
                                        AnimationConfig cfg) {
            var isCrash = isCrash(msr) ? "yes" : "no";
            double force = EnvironmentLunar.forceInNewton(env.getParameters().clippedForce(msr.action()));
            var data = Collections.singletonList(new Object[][]{
                    {"episode", ei + "(" + eiMax + ")"},
                    {"Force (N)", cfg.round(force)},
                    {"Pos (m)", cfg.round(msr.state().y())},
                    {"Speed (m/s)", cfg.round(msr.state().spd())},
                    {"is crash", isCrash},
            });
            return new TableData(data);
        }
    }


    static final int WIDTH = 300;
    static final int HEIGHT = 400;
    public static final int TABLE_HEIGHT = (int) (HEIGHT * 0.5);
    static final int X_LOCATION_ENV = 20;
    static final int X_LOCATION_VAL = 400;
    static final int N_COLUMNS = 2;
    static final int DIST_REF = 800;
    static final int DIST_DIFF = 30;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 10.0, 1000.0, 1010.0,  9990.0),  //cuts
            List.of(500.0, 0.0, 100.0, 0.0,     500.0)   //animation time delays
    );

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;
    SoundsCannon sounds;
    EnvironmentLunar env;
    AnimationConfig cfg;

    public static AnimationLunar create(AnimationConfig cfg, EnvironmentLunar env) {
        var asStep = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT, X_LOCATION_ENV);
        var asEpisode = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT, X_LOCATION_VAL);
        return new AnimationLunar(
                AnimationKit.of(environmentGfx(asStep), asStep),
                AnimationKit.of(episodeGfx(asEpisode), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                SoundsCannon.create(),
                env,
                cfg);
    }

    public static AnimationLunar empty() {
        return new AnimationLunar(
                AnimationKit.empty(),
                AnimationKit.empty(),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                SoundsCannon.create(),
                null,
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

    public void postStep(Pair<Integer, Integer> epis, MultiStepResult msr) {
        if (delayFunction.applyAsDouble(epis.getFirst())<1) return;

        List<LineSegment> lines = new ArrayList<>();
        addLunar(lines, msr);
        addFireDots(lines, msr);
        //sounds.playFire();

        ConditionalsUtil.executeIfTrue(isCrash(msr), () -> sounds.playSplat());

        var tableData = TableData.create(epis.getFirst(), epis.getSecond(), msr, env, cfg);
        postCommon(epis.getFirst(), lines, tableData);
    }


    public void postEpisode(MemoryCannon memory, int ei, Pair<Double, Double> retBase, GradientMeanAndLogStd grad) {
        if (isEmpty()) return;
        int nRows = 1;
        int nCols = 2;
        double[][] vGrid = MyMatrixArrayUtil.emptyMatrix(nRows, nCols);
        var scalerMean = ScalerLinear.of(0, 90, 0.0, 1.0);
        var scalerStd = ScalerLinear.of(0, 10, 0.0, 1.0);
        double expAngleDeg = UnitConverterUtil.convertRadiansToDegrees(memory.mean());
        double stdAngleDeg = UnitConverterUtil.convertRadiansToDegrees(memory.std());
        vGrid[0][0] = scalerMean.calcOutDouble(expAngleDeg);
        vGrid[0][1] = scalerStd.calcOutDouble(stdAngleDeg);
        List<double[][]> grids = new ArrayList<>();
        grids.add(GridFactory.toSeries(vGrid));
        var tableData = Collections.singletonList(new Object[][]{
                {"(" + "m,d" + ")", "(" + cfg.round(expAngleDeg) + "," + cfg.round(stdAngleDeg) + ")"},
                //   {"base", cfg.round(retBase.getSecond())},
                //   {"return-base", cfg.round(retBase.getFirst()-retBase.getSecond())},
                {"grad log", "(" + cfg.round(grad.mean()) + "," + cfg.round(grad.std()) + ")"},
        });
        kitEpisode.postAndSleep(GraphicsDto.dtoEpisode(grids, tableData, (int) delayFunction.applyAsDouble(ei)));
    }


    private void addLunar(List<LineSegment> lines, MultiStepResult msr) {
        var p = LunarParams.create(msr.state());
        //body
        lines.add(LineSegment.grey(p.left(), p.top(), p.right(), p.top()));       // top
        lines.add(LineSegment.grey(p.right(), p.top(), p.right(), p.bottom()));   // right side
        lines.add(LineSegment.grey(p.right(), p.bottom(), p.left(), p.bottom())); // bottom
        lines.add(LineSegment.grey(p.left(), p.bottom(), p.left(), p.top()));     // d.left() side

        System.out.println("p.topArm() = " + p.topArm1() + ", p.bottomArm() = " + p.bottomArm1());

        //angled arms
        lines.add(LineSegment.grey(p.left(), p.topArm1(), p.leftBottom1(), p.bottomArm1()));       // left side
        lines.add(LineSegment.grey(p.right(), p.topArm1(), p.rightBottom2(), p.bottomArm1()));   // right side

        //vertical arms
        lines.add(LineSegment.grey(p.leftBottom1(), p.bottomArm1(), p.leftBottom1(), p.bottomArm2())); // left side
        lines.add(LineSegment.grey(p.rightBottom2(), p.bottomArm1(), p.rightBottom2(), p.bottomArm2()));  // left side

    }


    private void addFireDots(List<LineSegment> lines, MultiStepResult msr) {
        double forceKN = env.getParameters().clippedForce(msr.action());
        var state = msr.state();
        var p = LunarParams.create(state);

        double relforce = forceKN / env.getParameters().forceMax();
        double radiusFire = p.radiusFire(relforce);
        System.out.println("radiusFire = " + radiusFire);
        for (int i = 0; i < p.nFireDots(relforce); i++) {
            var xyPos = p.randomPosInCircle(p.centerFire(relforce), radiusFire);
            lines.add(LineSegment.circleCommon(
                    xyPos.getFirst(), xyPos.getSecond(),
                    p.randomColor(), p.radiusFireDot()));
        }
    }




/*
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
    }*/

    private static boolean isCrash(MultiStepResult msr) {
        return msr.stepReturn().isFail();
    }

    private void postCommon(int ei, List<LineSegment> lines, TableData tableData) {
        if (isEmpty()) return;
        var lineData = List.of(lines);
        int animationDelay = (int) delayFunction.applyAsDouble(ei);
        var dto = GraphicsDto.dtoStep(lineData, tableData.data, animationDelay, false);
        kitStep.postAndSleep(dto);
    }


    private static GfxComponentFactory environmentGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addLineChart("",
                "x", Pair.create(-6, 6),
                "y", Pair.create(0, 8));
        styleChart(factory.getLineCharts().get(0));
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        var plot = chart.getXYPlot();
        plot.setBackgroundPaint(LunarParams.create(null).colorBackground());
        plot.getRangeAxis().setVisible(false);  // disable Y axis
        plot.setDomainGridlinesVisible(false); // vertical grid lines
        plot.setRangeGridlinesVisible(false);  // horizontal grid lines
    }

    private static GfxComponentFactory episodeGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addHeatMap("Agent value memory");
        var heatmap = factory.getHeatMapCharts().get(0);
        heatmap.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));
        var plot = heatmap.getXYPlot();
        plot.getDomainAxis().setVisible(false); // disable X axis
        plot.getRangeAxis().setVisible(false);  // disable Y axis

        factory.addHeatMap("Agent policy memory");
        heatmap = factory.getHeatMapCharts().get(1);
        heatmap.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));
        plot = heatmap.getXYPlot();
        plot.getDomainAxis().setVisible(false); // disable X axis
        plot.getRangeAxis().setVisible(false);  // disable Y axis

        factory.addTable(N_COLUMNS, false);
        return factory;
    }


}
