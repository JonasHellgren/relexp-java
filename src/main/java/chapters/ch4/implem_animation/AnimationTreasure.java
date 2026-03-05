package chapters.ch4.implem_animation;

import chapters.ch4.domain.animation.AnimationGridI;
import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import chapters.ch4.implem.treasure.core.InformerTreasure;
import core.animation.*;
import core.foundation.config.AnimationConfig;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.cond.ConditionalsUtil;
import core.foundation.util.math.MathUtil;
import core.gridrl.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;
import oshi.util.FormatUtil;

import java.util.*;
import java.util.function.DoubleUnaryOperator;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimationTreasure implements AnimationGridI {

    static final int HEIGHT = 300;
    static final int WIDTH = 300;
    static final int N_COLUMNS = 2;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 30.0, 99990.0),  //cuts
            List.of(100.0, 1.0, 500.0)   //animation time delays
    );

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;
    InformerGridParamsI informer;
    AnimationConfig cfg;

    public static AnimationTreasure empty() {
        return new AnimationTreasure(
                AnimationKit.empty(),
                AnimationKit.empty(),
                DelayIntervalFunction.from(IntervalData.empty()),
                InformerTreasure.empty(),
                AnimationConfig.defaults());
    }

    public static AnimationTreasure create(EnvironmentGridI env0, AnimationConfig cfg) {
        var env = (EnvironmentTreasure) env0;
        var asStep=AnimationSettings.of(cfg, WIDTH, HEIGHT, HEIGHT/2,200);
        var asEpisode=AnimationSettings.of(cfg, WIDTH, HEIGHT*2, HEIGHT,WIDTH*2)
                .withTableWidth((int) (WIDTH * 0.75));

        return new AnimationTreasure(
                AnimationKit.of(environmentGfx(asStep, env), asStep),
                AnimationKit.of(episodeGfx(asEpisode,env), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
                env.informer(),
                cfg);
    }

    @Override
    public boolean isEmpty() {
        return kitEpisode.isEmpty();
    }

    @Override
    public void start() {
        if (isEmpty()) return;
        kitStep.start();
        kitEpisode.start();
    }

    @Override
    public void postStep(StateGrid s, int ei, int eiMax, double pRand, double reward) {
        if (isEmpty()) return;
        List<LineSegment> lines = new ArrayList<>();
        addSeekerLines(s, lines);
        addFixedObjectsLines(lines);
        var lineData = List.of(lines);
        var tableData = Collections.singletonList(new Object[][]{
                {"episode", String.valueOf(ei)},
                {"number of episodes", String.valueOf(eiMax)},
                {"reward", reward},
                {"probability random action", round(pRand)},
                {"x pos", s.x()},
                {"y pos", s.y()}
        });
        var dto = GraphicsDto.dtoStep(
                lineData, tableData, (int) delayFunction.applyAsDouble(ei), reward < -99);
        kitStep.postAndSleep(dto);
    }

    private void addFixedObjectsLines(List<LineSegment> lines) {
        var xMinMax = informer.getPosXMinMax();
        var yMinMax = informer.getPosYMinMax();
        double ox = 0.5;  //x offset
        double oy = 0.5;  //y offset
        //cells
        for (int x = xMinMax.getFirst(); x <= xMinMax.getSecond(); x++) {
            for (int y = yMinMax.getFirst(); y <= yMinMax.getSecond(); y++) {
                var s = StateGrid.of(x, y);
                ConditionalsUtil.executeIfTrue(informer.isFail(s), () ->
                        lines.add(LineSegment.redBold(s.x(), s.y(), s.x(), s.y())));
                ConditionalsUtil.executeIfTrue(informer.isWall(s), () -> {
                    lines.add(LineSegment.black(s.x() - ox, s.y() - oy, s.x() - ox, s.y() + oy));
                    lines.add(LineSegment.black(s.x() + ox, s.y() - oy, s.x() + ox, s.y() + oy));
                    lines.add(LineSegment.black(s.x() - ox, s.y() - oy, s.x() + ox, s.y() - oy));
                    lines.add(LineSegment.black(s.x() - ox, s.y() + oy, s.x() + ox, s.y() + oy));
                });
                ConditionalsUtil.executeIfTrue(s.equals(StateGrid.of(4, 0)), () ->
                        lines.add(LineSegment.goldSmall(s.x(), s.y())));
                ConditionalsUtil.executeIfTrue(s.equals(StateGrid.of(9, 1)), () ->
                        lines.add(LineSegment.goldBig(s.x(), s.y())));
            }
        }

        //surrounding walls
        var xmin = xMinMax.getFirst() - ox;
        var xmax = xMinMax.getSecond() + ox;
        var ymin = yMinMax.getFirst() - oy;
        var ymax = yMinMax.getSecond() + oy;
        lines.add(LineSegment.black(xmin, ymin, xmin, ymax));
        lines.add(LineSegment.black(xmax, ymin, xmax, ymax));
        lines.add(LineSegment.black(xmin, ymin, xmax, ymin));
        lines.add(LineSegment.black(xmin, ymax, xmax, ymax));


    }

    @Override
    public void postEpisode(AgentGridI agent, EnvironmentGridI env0) {
        if (isEmpty()) return;
        int nCol = informer.getPosXMinMax().getSecond();
        int nRows = informer.getPosYMinMax().getSecond()+ 1;
        double vMin = -1;
        double vMax = 10*1.5;
        var scaler = ScalerLinear.of(vMin, vMax, 0.0, 1.0);
        Map<ActionGrid, double[][]> aGrids = new HashMap<>();
        informer.getValidActions().forEach(ay -> aGrids.put(ay, emptyGrid(nRows, nCol)));
        double[][] vGrid = emptyGrid(nRows, nCol);
        Object[][] policyGrid = new Object[nRows][nCol];
        for (int x = 0; x < nCol; x++) {
            for (int y = 0; y < nRows; y++) {
                var s = StateGrid.of(x, y);
                double value = agent.readValue(s);
                vGrid[y][x] = scale(scaler, value);
                policyGrid[nRows - 1 - y][x] = agent.chooseActionNoExploration(s).toString();
                for (ActionGrid a : informer.getValidActions()) {
                    double av = agent.read(s, a);
                    aGrids.get(a)[y][x] = scale(scaler, av);
                }
            }
        }

        List<double[][]> grids = new ArrayList<>();
        informer.getValidActions().forEach(a -> grids.add(GridFactory.toSeries(aGrids.get(a))));
        grids.add(GridFactory.toSeries(vGrid));
        var dto = GraphicsDto.dtoEpisode(grids, policyGrid);
        kitEpisode.postAndSleep(dto);
    }

    private static void addSeekerLines(StateGrid s, List<LineSegment> lines) {
        double x0 = s.x();
        double y0 = s.y();
        lines.add(LineSegment.blackBold(x0, y0, x0, y0));
    }

    private static GfxComponentFactory environmentGfx(AnimationSettings as, EnvironmentGridI env) {
        var factory = GfxComponentFactory.of(as);
        var posxMinMax = env.informer().getPosXMinMax();
        var posyMinMax = env.informer().getPosYMinMax();
        factory.addLineChart("",
                "x", Pair.create(-1, posxMinMax.getSecond() + 1),
                "y", Pair.create(-1, posyMinMax.getSecond() + 1));
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static   GfxComponentFactory episodeGfx(AnimationSettings as, EnvironmentTreasure env) {
        var informer = env.informer();
        var factory = GfxComponentFactory.of(as);
        factory.addHeatMap("N");
        factory.addHeatMap("E");
        factory.addHeatMap("S");
        factory.addHeatMap("W");
        factory.addHeatMap("Value");
        factory.addTable(informer.getPosXMinMax().getSecond(), true);
        return factory;
    }

    private static double scale(ScalerLinear scaler, double value) {
        return scaler.calcOutDouble(MathUtil.clip(value, scaler.d0, scaler.d1));
    }

    private static double[][] emptyGrid(Integer nRows, Integer nCol) {
        return new double[nRows][nCol];
    }

    private  float round(double pRand) {
        return FormatUtil.round((float) pRand,cfg.ndigits() );
    }

}
