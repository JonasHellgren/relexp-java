package chapters.ch11.animation;

import chapters.ch11.domain.agent.core.AgentLunar;
import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.trainer.multisteps.MultiStepResult;
import core.animation.*;
import core.foundation.config.AnimationConfig;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.cond.ConditionalsUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;
import org.jfree.chart.JFreeChart;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimationLunar {

    record EnvironmentTableData(List<Object[][]> data) {
        private static EnvironmentTableData create(int ei,
                                                   int eiMax,
                                                   MultiStepResult msr,
                                                   EnvironmentLunar env,
                                                   AnimationConfig cfg) {
            var isCrash = isCrash(msr) ? "yes" : "no";
            double force = EnvironmentLunar.forceInNewton(env.getParameters().clippedForce(msr.action()));
            var data = Collections.singletonList(new Object[][]{
                    {"episode", ei + " (" + eiMax + ")"},
                    {"Force (N)", cfg.round(force)},
                    {"Acceleration (m/s2)", cfg.round(env.acceleration(msr.action()))},
                    {"Speed (m/s)", cfg.round(msr.state().spd())},
                    {"Pos (m)", cfg.round(msr.state().y())},
                    {"Advantage", cfg.round(msr.advantage())},
                    {"Value", cfg.round(msr.valueTarget())},
                    {"reward (is crash?)", cfg.round(msr.stepReturn().reward()) + " (" + isCrash + ")"},
            });
            return new EnvironmentTableData(data);
        }
    }

    static final int POST_EPIS_INTERVALL = 5;
    static final int WIDTH = 300;
    static final int HEIGHT = 400;
    public static final int N_COL_ROWS_HEAT_MAP = 50;
    public static final int TABLE_HEIGHT_ENV = (int) (HEIGHT * 0.5);
    public static final int TABLE_HEIGHT_EPIS = (int) (HEIGHT * 0.1);
    static final int X_LOCATION_ENV = 20;
    static final int X_LOCATION_VAL = 400;
    static final int N_COLUMNS = 2;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0,    5.0,   3000.0, 3003.0, 9995.0),  //cuts
            List.of(250.0,  5.0,    250.0,  0.0,    250.0)   //animation time delays
    );

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;
    SoundsLunar sounds;
    EnvironmentLunar env;
    AnimationConfig cfg;

    public static AnimationLunar create(AnimationConfig cfg, EnvironmentLunar env) {
        var asStep = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT_ENV, X_LOCATION_ENV);
        var asEpisode = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT_EPIS, X_LOCATION_VAL);
        return new AnimationLunar(
                AnimationKit.of(environmentGfx(asStep), asStep),
                AnimationKit.of(episodeGfx(asEpisode,env), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                SoundsLunar.create(),
                env,
                cfg);
    }

    public static AnimationLunar empty() {
        return new AnimationLunar(
                AnimationKit.empty(),
                AnimationKit.empty(),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                SoundsLunar.create(),
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
        if (kitStep.isEmpty()) return;
        if (delayFunction.applyAsDouble(epis.getFirst()) < 10.0) return;

        List<LineSegment> lines = new ArrayList<>();
        addBackground(lines, msr);
        addLunar(lines, msr);
        addFireDots(lines, msr);
        var par = env.getParameters();
        sounds.setVolumeForce((float) (par.clippedForce(msr.action())/par.forceMax()));
        sounds.playForce();
        ConditionalsUtil.executeIfTrue(isCrash(msr), () -> sounds.playCrash());
        ConditionalsUtil.executeIfTrue(isSafeLanded(msr), () -> sounds.playNiceLanding());
        var tableData = EnvironmentTableData.create(epis.getFirst(), epis.getSecond(), msr, env, cfg);
        postCommon(epis.getFirst(), lines, tableData);
    }

    public void postEpisode(AgentLunar agent,int ei) {
        if (kitStep.isEmpty()) return;

        if (isEmpty() || (ei % POST_EPIS_INTERVALL != 0)) return;
        System.out.println("ei = " + ei);
        var scaler = ScalerLinear.of(-50, 100.0, 0.0, 1.0);
        double[][] vGrid = getData(s -> agent.readCritic(s),scaler);
        var scalerPol = ScalerLinear.of(-2,3, 0.0, 1.0);
        double[][] polGrid = getData(s -> env.acceleration(agent.readActor(s).mean()),scalerPol);

        var yList = env.getParameters().ySpace(N_COL_ROWS_HEAT_MAP);
        var spdList = env.getParameters().spdSpace(N_COL_ROWS_HEAT_MAP);
        List<double[][]> grids = new ArrayList<>();
        grids.add(GridFactory.toSeries(vGrid, spdList, yList));
        grids.add(GridFactory.toSeries(polGrid, spdList, yList));
        var tableData = Collections.singletonList(new Object[][]{{"episode:", ei}});
        kitEpisode.postAndSleep(
                GraphicsDto.dtoEpisode(grids, tableData, (int) delayFunction.applyAsDouble(ei)));
    }


    private double[][] getData(Function<StateLunar,Double> func, ScalerLinear scaler) {
        var yList = env.getParameters().ySpace(N_COL_ROWS_HEAT_MAP);
        var spdList = env.getParameters().spdSpace(N_COL_ROWS_HEAT_MAP);
        double[][] data = new double[yList.size()][spdList.size()];
        for (double y : yList) {
            for (double spd : spdList) {
                int yi = yList.indexOf(y);
                int spdi = spdList.indexOf(spd);
                data[yi][spdi] = scaler.calcOutDouble(func.apply(StateLunar.of(y, spd)));
            }
        }
        return data;
    }

    private void addBackground(List<LineSegment> lines, MultiStepResult msr) {
        var p = LunarParams.create(msr.state());
        for (Pair<Double,Double> pos : p.stars()) {
            lines.add(LineSegment.circleCommon(pos.getFirst(),pos.getSecond(),Color.WHITE,2));
        }
        lines.add(LineSegment.line(-p.xmax(), 0, p.xmax(), 0, Color.GRAY, 10));
    }

    private void addLunar(List<LineSegment> lines, MultiStepResult msr) {
        var p = LunarParams.create(msr.state(),isCrash(msr));

        //body
        lines.add(LineSegment.grey(p.left(), p.top(), p.right(), p.top()));       // top
        lines.add(LineSegment.grey(p.right(), p.top(),p.right(), p.bottom()));   // right side
        lines.add(LineSegment.grey(p.right(), p.bottom(),p.left(), p.bottom())); // bottom
        lines.add(LineSegment.grey(p.left(), p.bottom(), p.left(), p.top()));     // d.left() side

        //antenna
        lines.add(LineSegment.grey(p.midx(), p.topAntenna(), p.midx(), p.top()));

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
        for (int i = 0; i < p.nFireDots(relforce); i++) {
            var xyPos = p.randomPosInCircle(p.centerFire(relforce), radiusFire);
            lines.add(LineSegment.circleCommon(
                    xyPos.getFirst(), xyPos.getSecond(),
                    p.randomColor(), p.randomRadiusFireDot()));
        }
    }

    private static boolean isCrash(MultiStepResult msr) {
        return msr.stepReturn().isFail();
    }

    private boolean isSafeLanded(MultiStepResult msr) {
        return msr.stepReturn().isTerminal() && !isCrash(msr);

    }

    private void postCommon(int ei, List<LineSegment> lines, EnvironmentTableData tableData) {
        if (isEmpty()) return;
        var lineData = List.of(lines);
        int animationDelay = (int) delayFunction.applyAsDouble(ei);
        var dto = GraphicsDto.dtoStep(lineData, tableData.data, animationDelay, false);
        kitStep.postAndSleep(dto);
    }


    private static GfxComponentFactory environmentGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        var p = LunarParams.empty();

        factory.addLineChart("",
                "x", Pair.create(-p.xmax(), p.xmax()),
                "y", Pair.create(0, p.ymax()));
        styleChart(factory.getLineCharts().get(0));
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        var plot = chart.getXYPlot();
        plot.setBackgroundPaint(LunarParams.empty().colorBackground());
        plot.getDomainAxis().setVisible(false); // disable X axis
        plot.setDomainGridlinesVisible(false); // vertical grid lines
        plot.setRangeGridlinesVisible(false);  // horizontal grid lines
    }

    private static GfxComponentFactory episodeGfx(AnimationSettings as,EnvironmentLunar env) {
        var factory = GfxComponentFactory.of(as);
        factory.addHeatMap("Agent value memory");
        var heatmap = factory.getHeatMapCharts().get(0);
        styleMap(heatmap,env);
        factory.addHeatMap("Agent policy memory");
        heatmap = factory.getHeatMapCharts().get(1);
        styleMap(heatmap,env);
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static void styleMap(JFreeChart heatmap, EnvironmentLunar env) {
        heatmap.getTitle().setFont(new Font("SansSerif", Font.BOLD, 12));
        var plot = heatmap.getXYPlot();
        plot.getDomainAxis().setLabel("Speed (m/s)");
        plot.getRangeAxis().setLabel("Position (m)");

    }


}
