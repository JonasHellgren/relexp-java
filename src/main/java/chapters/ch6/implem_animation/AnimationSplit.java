package chapters.ch6.implem_animation;

import chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath;
import chapters.ch4.implem.treasure.core.InformerTreasure;
import chapters.ch6.domain.agent.AgentGridMultiStepI;
import chapters.ch6.domain.animation.AnimationGridMultiStepI;
import core.animation.*;
import core.foundation.config.AnimationConfig;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.math.MathUtil;
import core.gridrl.EnvironmentGridI;
import core.gridrl.InformerGridParamsI;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimationSplit implements AnimationGridMultiStepI {
    static final int HEIGHT_ENV = 200;
    public static final int TABLE_HEIGHT_ENV = (int) (HEIGHT_ENV * 0.35);
    static final int HEIGHT_VAL = 200;
    static final int WIDTH = 300;
    static final int X_LOCATION_ENV = 100;
    static final int X_LOCATION_VAL = 500;
    static final int N_COLUMNS = 2;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 10.0, 100.0),  //cuts
            List.of(500.0, 1.0, 500.0)   //animation time delays
    );

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;
    InformerGridParamsI informer;
    AnimationConfig cfg;

    public static AnimationSplit empty() {
        return new AnimationSplit(
                AnimationKit.empty(),
                AnimationKit.empty(),
                DelayIntervalFunction.from(IntervalData.empty()),
                InformerTreasure.empty(),
                AnimationConfig.defaults());
    }

    public static AnimationSplit create(EnvironmentGridI env0, AnimationConfig cfg) {
        var env = (EnvironmentSplittingPath) env0;
        var asStep = AnimationSettings.of(cfg,WIDTH,HEIGHT_ENV, TABLE_HEIGHT_ENV, X_LOCATION_ENV);
        var asEpisode = AnimationSettings.of(cfg,WIDTH, HEIGHT_VAL, 0, X_LOCATION_VAL);
        return new AnimationSplit(
                AnimationKit.of(environmentGfx(asStep, env), asStep),
                AnimationKit.of(episodeGfx(asEpisode), asEpisode),
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
    public void postStep(StateGrid s, int ei, int eiMax, double pRand, double sumReward) {
        if (isEmpty()) return;
        List<LineSegment> lines = new ArrayList<>();
        addSeekerLines(s, lines);
        var lineData = List.of(lines);
        var tableData = Collections.singletonList(new Object[][]{
                {"episode", ei+"("+eiMax+")"},
                {"sum rewards", sumReward},
                {"(x,y) pos", "("+s.x()+","+s.y()+")"}
        });
        var dto = GraphicsDto.dtoStep(
                lineData, tableData, (int) delayFunction.applyAsDouble(ei), false);
        kitStep.postAndSleep(dto);
    }


    @Override
    public void postEpisode(AgentGridMultiStepI agent, EnvironmentGridI env0,int ei) {
        if (isEmpty()) return;
        int nCol = informer.getPosXMinMax().getSecond();
        int nRows = informer.getPosYMinMax().getSecond()+ 1;;
        double vMin = 0;
        double vMax = 1;
        var scaler = ScalerLinear.of(vMin, vMax, 0.0, 1.0);
        double[][] vGrid = emptyGrid(nRows, nCol);
        for (int x = 0; x < nCol; x++) {
            for (int y = 0; y < nRows; y++) {
                var s = StateGrid.of(x, y);
                double value = agent.read(s);
                vGrid[y][x] = scale(scaler, value);
            }
        }

        List<double[][]> grids = new ArrayList<>();
        grids.add(GridFactory.toSeries(vGrid));
        var emptyTable = Collections.singletonList(new Object[][]{});
        kitEpisode.postAndSleep(GraphicsDto.dtoEpisode(grids,emptyTable, 0));
    }

    private static GfxComponentFactory environmentGfx(AnimationSettings as, EnvironmentGridI env) {
        var factory = GfxComponentFactory.of(as);
        var posxMinMax = env.informer().getPosXMinMax();
        var posyMinMax = env.informer().getPosYMinMax();
        factory.addLineChart("",
                "x", Pair.create(0, posxMinMax.getSecond()-1),
                "y", Pair.create(-1, posyMinMax.getSecond()+1));
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static GfxComponentFactory episodeGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addHeatMap("Value");
        var heatmap = factory.getHeatMapCharts().get(0);
        heatmap.getTitle().setFont(new Font("SansSerif", Font.BOLD, 12));
        return factory;
    }

    private static void addSeekerLines(StateGrid s, List<LineSegment> lines) {
        double x0 = s.x();
        double y0 = s.y();
        lines.add(LineSegment.blackBold(x0, y0, x0, y0));
    }

    private static double scale(ScalerLinear scaler, double value) {
        return scaler.calcOutDouble(MathUtil.clip(value, scaler.d0, scaler.d1));
    }

    private static double[][] emptyGrid(Integer nRows, Integer nCol) {
        return new double[nRows][nCol];
    }

}
