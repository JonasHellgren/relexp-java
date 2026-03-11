package chapters.ch10.animation_bandit;

import chapters.ch10.bandit.domain.agent.MemoryBandit;
import chapters.ch10.bandit.domain.environment.ActionBandit;
import chapters.ch10.bandit.domain.trainer.ExperienceBandit;
import core.animation.*;
import core.foundation.config.AnimationConfig;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.collections.MyMatrixArrayUtil;
import core.foundation.util.cond.ConditionalsUtil;
import core.foundation.util.formatting.NumberFormatterUtil;
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
public class AnimationBandit {

    static final int HEIGHT_ENV = 200;
    public static final int TABLE_HEIGHT = (int) (HEIGHT_ENV * 0.5);
    static final int HEIGHT_VAL = 200;
    static final int WIDTH = 300;
    static final int X_LOCATION_ENV = 100;
    static final int X_LOCATION_VAL = 500;
    static final int N_COLUMNS = 2;


    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 10.0, 490.0),  //cuts
            List.of(1000.0, 1.0, 1000.0)   //animation time delays
    );
    public static final int Z_MAX = 5;

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;
    Sounds sounds;
    AnimationConfig cfg;

    public static AnimationBandit create(AnimationConfig cfg) {
        var asStep = AnimationSettings.of(cfg, WIDTH, HEIGHT_ENV, TABLE_HEIGHT, X_LOCATION_ENV);
        var asEpisode = AnimationSettings.of(cfg, WIDTH, HEIGHT_VAL, TABLE_HEIGHT, X_LOCATION_VAL);
        return new AnimationBandit(
                AnimationKit.of(environmentGfx(asStep), asStep),
                AnimationKit.of(episodeGfx(asEpisode), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                Sounds.create(),
                cfg);
    }

    public static AnimationBandit empty() {
        return new AnimationBandit(
                AnimationKit.empty(),
                AnimationKit.empty(),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                Sounds.create(),
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

    public void postStep(int ei, int eiMax, List<ExperienceBandit> experiences) {
        postCommon(ei, eiMax, experiences, true);
        var exp = experiences.get(0);
        ConditionalsUtil.executeIfTrue(exp.stepReturn().isCoin(),
                () -> sounds.playCoin());
    }

    public void postAfterStep(int ei, int eiMax, List<ExperienceBandit> experiences) {
        postCommon(ei, eiMax, experiences, false);

    }

    private void postCommon(int ei, int eiMax, List<ExperienceBandit> experiences, boolean isStep) {
        if (isEmpty()) return;
        List<LineSegment> lines = new ArrayList<>();
        var exp = experiences.get(0);
        addBanditLines(lines,isStep);
        ConditionalsUtil.executeIfTrue(isStep,
                () -> addArmsAndCoin(experiences.get(0), lines));
        var lineData = List.of(lines);
        double reward = exp.reward();
        String actionLorR = exp.action().toString();
        String isCoin = exp.stepReturn().isCoin() ? "yes" : "no";
        var tableData = Collections.singletonList(new Object[][]{
                {"episode", ei + "(" + eiMax + ")"},
                {"action", actionLorR},
                {"coin achieved", isCoin},
                {"reward", reward},
        });
        int animationDelay = isStep ?  0: (int) delayFunction.applyAsDouble(ei);
        var dto = GraphicsDto.dtoStep(lineData, tableData, animationDelay, false);
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
                {"z", "(" + round(z[0]) + "," + round(z[1]) + ")"},
                {"return", round(returnAtT)},
                {"grad log", "(" + round(gradLog[0]) + "," + round(gradLog[1]) + ")"},
                {"probability (L,R)", "(" + round(probArray[0]) + "," + round(probArray[1]) + ")"},

        });

        kitEpisode.postAndSleep(GraphicsDto.dtoEpisode(grids, tableData, (int) delayFunction.applyAsDouble(ei)));
    }

    private static double round(double z) {
        return NumberFormatterUtil.roundTo1Decimals(z);
    }

    private static GfxComponentFactory environmentGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addLineChart("",
                "x", Pair.create(-40, 90),
                "y", Pair.create(0, 65));
        styleBanditChart(factory.getLineCharts().get(0));
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static void styleBanditChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        var plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.getDomainAxis().setVisible(false); // disable X axis
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
        var p = BanditParams.create();
        // Machine body
        lines.add(LineSegment.black(p.left(), p.top(), p.right(), p.top()));       // top
        lines.add(LineSegment.black(p.right(), p.top(), p.right(), p.bottom()));   // right side
        lines.add(LineSegment.black(p.right(), p.bottom(), p.left(), p.bottom())); // bottom
        lines.add(LineSegment.black(p.left(), p.bottom(), p.left(), p.top()));     // d.left() side
        // window
        lines.add(LineSegment.grey(p.windowLeft(), p.windowTop(), p.windowRight(), p.windowTop()));       // window top
        lines.add(LineSegment.grey(p.windowLeft(), p.windowBottom(), p.windowRight(), p.windowBottom())); // window bottom
        lines.add(LineSegment.grey(p.windowLeft(), p.windowBottom(), p.windowLeft(), p.windowTop())); // window bottom
        lines.add(LineSegment.grey(p.windowRight(), p.windowBottom(), p.windowRight(), p.windowTop())); // window bottom
        // coin dispenser
        lines.add(LineSegment.black(p.dispLeft(), p.dispY(), p.dispRight(), p.dispY()));       // dispenser bottom
        lines.add(LineSegment.black(p.dispLeft(), p.dispY(), p.dispLeft(), p.dispYtop()));       // dispenser bottom
        lines.add(LineSegment.black(p.dispRight(), p.dispY(), p.dispRight(), p.dispYtop()));       // dispenser bottom
        //buttons below window
        lines.add(LineSegment.black(p.left(), p.panelHigh(), p.right(), p.panelHigh()));
        lines.add(LineSegment.black(p.left(), p.panelLow(), p.right(), p.panelLow()));
        lines.add(LineSegment.circleSmall(p.xRel(0.2), p.buttonY(),p.colorButtons()));
        lines.add(LineSegment.circleSmall(p.xRel(0.5), p.buttonY(),p.colorButtons()));
        lines.add(LineSegment.circleSmall(p.xRel(0.8), p.buttonY(),p.colorButtons()));
        //arm connections
        lines.add(LineSegment.black(p.armLeftXPos(), p.topArm(), p.left(), p.topArm()));
        lines.add(LineSegment.black(p.armRightXPos(), p.topArm(), p.right(), p.topArm()));
        //knobs
        if (!isStep) {  //only show after step (reset arm pos)
            lines.add(LineSegment.circle(p.armLeftXPos(), p.topArm(), p.colorKnob()));
            lines.add(LineSegment.circle(p.armRightXPos(), p.topArm(), p.colorKnob()));
        }

    }

    private static void addArmsAndCoin(ExperienceBandit exp, List<LineSegment> lines) {
        var p = BanditParams.create();
        if (exp.action() == ActionBandit.LEFT) {
            lines.add(LineSegment.black(p.armLeftXPos(), p.topArm(), p.armLeftXPos(), p.bottomArm()));
            lines.add(LineSegment.circle(p.armLeftXPos(), p.bottomArm(), p.colorKnob()));
            lines.add(LineSegment.circle(p.armRightXPos(), p.topArm(), p.colorKnob()));
        } else {
            lines.add(LineSegment.black(p.armRightXPos(), p.topArm(), p.armRightXPos(), p.bottomArm()));
            lines.add(LineSegment.circle(p.armRightXPos(), p.bottomArm(), p.colorKnob()));
            lines.add(LineSegment.circle(p.armLeftXPos(), p.topArm(), p.colorKnob()));
        }
        if (exp.stepReturn().isCoin()) {
            lines.add(LineSegment.circle(p.coinX(), p.coinY(), Color.orange));
        }
    }

}
