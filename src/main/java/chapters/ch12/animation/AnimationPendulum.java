package chapters.ch12.animation;

import chapters.ch11.domain.agent.core.AgentLunar;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.trainer.multisteps.MultiStepResult;
import chapters.ch12.domain.inv_pendulum.agent.core.AgentPendulum;
import chapters.ch12.domain.inv_pendulum.agent.memory.ActionAndItsValue;
import chapters.ch12.domain.inv_pendulum.environment.core.ActionPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.EnvironmentPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StepReturnPendulum;
import chapters.ch12.domain.inv_pendulum.trainer.core.ExperiencePendulum;
import core.animation.*;
import core.foundation.config.AnimationConfig;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.collections.ListCreatorUtil;
import core.foundation.util.cond.ConditionalsUtil;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.foundation.util.rand.RandUtil;
import core.foundation.util.unit_converter.UnitConverterUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimationPendulum {

    public static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 12);

    record EnvironmentTableData(List<Object[][]> data) {
        private static EnvironmentTableData create(int ei,
                                                   int eiMax,
                                                   ExperiencePendulum exp,
                                                   double val, double probRand,
                                                   AnimationConfig cfg) {
            var isFail = exp.stepReturn().isFail() ? "yes" : "no";
            var data = Collections.singletonList(new Object[][]{
                    {"episode", ei + " (" + eiMax + ")"},
                    {"Torque (Nm)", exp.action().torque()},
                    {"Ang speed (rad/s)", cfg.round(exp.state().angularSpeed())},
                    {"Angle (rad)", cfg.round(exp.state().angle())},
                    {"Num. of steps", cfg.round(exp.state().nSteps())},
                    {"Value", cfg.round(val)},
                    {"Probability random action", cfg.round(probRand)},
                    {"Reward (is fail?)", NumberFormatterUtil.roundToNDecimals(exp.stepReturn().reward(),3)
                            + " (" + isFail + ")"},
            });
            return new EnvironmentTableData(data);
        }
    }

    static final int WIDTH = 300;
    static final int HEIGHT = 400;
    public static final int N_COL_ROWS_HEAT_MAP = 70;
    public static final int TABLE_HEIGHT_ENV = (int) (HEIGHT * 0.5);
    public static final int TABLE_HEIGHT_EPIS = (int) (HEIGHT * 0.15);
    static final int X_LOCATION_ENV = 20;
    static final int X_LOCATION_VAL = 400;
    static final int N_COLUMNS = 2;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 10.0, 245.0),  //cuts
            List.of(250.0, 1.0, 250.0)   //animation time delays
    );

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;
    SoundsPendulum sounds;
    EnvironmentPendulum env;
    AnimationConfig cfg;

    public static AnimationPendulum create(AnimationConfig cfg, EnvironmentPendulum env) {
        var asStep = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT_ENV, X_LOCATION_ENV);
        var asEpisode = AnimationSettings.of(cfg, WIDTH, HEIGHT, TABLE_HEIGHT_EPIS, X_LOCATION_VAL);
        return new AnimationPendulum(
                AnimationKit.of(environmentGfx(asStep), asStep),
                AnimationKit.of(episodeGfx(asEpisode, env), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                SoundsPendulum.create(),
                env,
                cfg);
    }

    public static AnimationPendulum empty() {
        return new AnimationPendulum(
                AnimationKit.empty(),
                AnimationKit.empty(),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                SoundsPendulum.create(),
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

    public void postStep(Pair<Integer, Integer> epis, ExperiencePendulum exp, double val, double probRand) {
        if (kitStep.isEmpty()) return;

        List<LineSegment> lines = new ArrayList<>();
        addPendulum(lines, exp);
        addJoint(lines);
        addTorqueLine(lines, exp);
        var sr = exp.stepReturn();
        ConditionalsUtil.executeIfTrue(sr.isFail(), () -> sounds.playFail());
        ConditionalsUtil.executeIfTrue(sr.isTerminal() && !sr.isFail(), () -> sounds.playSuccess());
        var tableData = EnvironmentTableData.create(epis.getFirst(), epis.getSecond(), exp, val, probRand, cfg);
        postCommon(epis.getFirst(), lines, tableData);
    }


    public void postEpisode(AgentPendulum agent, int ei, int rpSize) {
        if (kitStep.isEmpty()) return;

        var scalerVal = ScalerLinear.of(-1.0, 0.1, 0.0, 1.0);
        double[][] vGrid = getData(s -> agent.readValue(s), scalerVal);
        var scalerPol = ScalerLinear.of(-2, 2, 0.0, 1.0);
        double[][] polGrid = getData(s -> agent.chooseActionNoExploration(s).torque(), scalerPol);

        var angleList = ListCreatorUtil.createFromStartToEndWithNofItems(-40, 40, N_COL_ROWS_HEAT_MAP);
        var spdList = ListCreatorUtil.createFromStartToEndWithNofItems(-40, 40, N_COL_ROWS_HEAT_MAP);
        List<double[][]> grids = new ArrayList<>();
        grids.add(GridFactory.toSeries(vGrid, spdList, angleList));
        grids.add(GridFactory.toSeries(polGrid, spdList, angleList));
        var tableData = Collections.singletonList(new Object[][]{
                {"Episode:", ei},
                {"Replay buffer size:", rpSize}}
        );
        kitEpisode.postAndSleep(
                GraphicsDto.dtoEpisode(grids, tableData, (int) delayFunction.applyAsDouble(ei)));
    }


    private double[][] getData(Function<StatePendulum, Double> func, ScalerLinear scaler) {
        var angleList = ListCreatorUtil.createFromStartToEndWithNofItems(-40, 40, N_COL_ROWS_HEAT_MAP);
        var spdList = ListCreatorUtil.createFromStartToEndWithNofItems(-40, 40, N_COL_ROWS_HEAT_MAP);
        double[][] data = new double[angleList.size()][spdList.size()];
        for (double a : angleList) {
            for (double spd : spdList) {
                int ai = angleList.indexOf(a);
                int spdi = spdList.indexOf(spd);
                double aRad = UnitConverterUtil.convertDegreesToRadians(a);
                double spdRad = UnitConverterUtil.convertDegreesToRadians(spd);
                data[ai][spdi] = scaler.calcOutDouble(func.apply(StatePendulum.ofStart(aRad, spdRad)));
            }
        }
        return data;
    }

    private void addJoint(List<LineSegment> lines) {
        var p = PendulumParams.empty();
        var top = p.jointTop();
        var left = p.jointLeft();
        var right = p.jointRight();
        lines.add(LineSegment.line(top.x(), top.y(), left.x(), left.y(), p.jointColor(), p.jointThickness()));
        lines.add(LineSegment.line(left.x(), left.y(), right.x(), right.y(), p.jointColor(), p.jointThickness()));
        lines.add(LineSegment.line(right.x(), right.y(), top.x(), top.y(), p.jointColor(), p.jointThickness()));
    }

    private void addPendulum(List<LineSegment> lines, ExperiencePendulum exp) {
        var p = PendulumParams.create(exp, env);
        var top = p.topArmXy();
        var bottom = p.bottomArmXy();
        lines.add(LineSegment.line(bottom.x(), bottom.y(), top.x(), top.y(), p.armColor(), p.armThikness()));
    }


    private void addTorqueLine(List<LineSegment> lines, ExperiencePendulum exp) {
        var p = PendulumParams.empty();
        var top = p.jointTop();
        var left = p.jointLeft();
        var right = p.jointRight();
        int arrowShift = 3;

        if (exp.action().equals(ActionPendulum.CCW)) {
            lines.add(LineSegment.line(right.x(), top.y(), top.x(), top.y(), p.torqueColor(), p.torqueThikness()));
            lines.add(LineSegment.line(top.x() + arrowShift, top.y() + arrowShift, top.x(), top.y(), p.torqueColor(), p.torqueThikness()));
            lines.add(LineSegment.line(top.x() + arrowShift, top.y() - arrowShift, top.x(), top.y(), p.torqueColor(), p.torqueThikness()));
        }

        if (exp.action().equals(ActionPendulum.CW)) {
            lines.add(LineSegment.line(left.x(), top.y(), top.x(), top.y(), p.torqueColor(), p.torqueThikness()));
            lines.add(LineSegment.line(top.x() - arrowShift, top.y() + arrowShift, top.x(), top.y(), p.torqueColor(), p.torqueThikness()));
            lines.add(LineSegment.line(top.x() - arrowShift, top.y() - arrowShift, top.x(), top.y(), p.torqueColor(), p.torqueThikness()));
        }


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
        var p = PendulumParams.empty();

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
        plot.setBackgroundPaint(PendulumParams.empty().colorBackground());
        plot.getRangeAxis().setVisible(false); // disable Y axis
        plot.getDomainAxis().setVisible(false); // disable X axis
        plot.setDomainGridlinesVisible(false); // vertical grid lines
        plot.setRangeGridlinesVisible(false);  // horizontal grid lines
    }

    private static GfxComponentFactory episodeGfx(AnimationSettings as, EnvironmentPendulum env) {
        var factory = GfxComponentFactory.of(as);
        factory.addHeatMap("Value");
        var heatmap = factory.getHeatMapCharts().get(0);
        styleMap(heatmap);
        factory.addHeatMap("Policy");
        heatmap = factory.getHeatMapCharts().get(1);
        styleMap(heatmap);
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static void styleMap(JFreeChart heatmap) {
        heatmap.getTitle().setFont(FONT_TITLE);
        var plot = heatmap.getXYPlot();
        plot.getDomainAxis().setLabel("Speed (rad/s)");
        plot.getRangeAxis().setLabel("Angle (rad)");
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        var axisD = plot.getDomainAxis();
        axisD.setLabelFont(FONT_LABEL);
        var axisR = plot.getRangeAxis();
        axisR.setLabelFont(FONT_LABEL);
    }


}
