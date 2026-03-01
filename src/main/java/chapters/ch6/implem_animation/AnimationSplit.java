package chapters.ch6.implem_animation;

import chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath;
import chapters.ch4.implem.treasure.core.InformerTreasure;
import chapters.ch6.domain.agent.AgentGridMultiStepI;
import chapters.ch6.domain.animation.AnimationGridMultiStepI;
import core.animation.*;
import core.foundation.config.AnimationConfig;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.math.MathUtil;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridI;
import core.gridrl.InformerGridParamsI;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.function.DoubleUnaryOperator;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimationSplit implements AnimationGridMultiStepI {
    static final int HEIGHT = 200;
    static final int WIDTH = 300;
    static final int FRAME_X_LOCATION = 100;
    static final int N_COLUMNS = 2;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 30.0, 99990.0),  //cuts
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
        var asStep = createSetting(cfg);
        var asEpisode = createSetting(cfg)
                .withFrameXLocation(FRAME_X_LOCATION).withFrameHeight(HEIGHT);
                //.withTableWidth((int) (WIDTH * 0.75)).withTableHeight((int) (HEIGHT * 0.1));
        return new AnimationSplit(
                AnimationKit.of(environmentGfx(asStep, env), asStep),
                AnimationKit.of(episodeGfx(asEpisode, env), asEpisode),
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
    }

    @Override
    public void postEpisode(AgentGridMultiStepI agent, EnvironmentGridI env0,int ei) {
        if (isEmpty()) return;
        int nCol = informer.getPosXMinMax().getSecond();
        int nRows = informer.getPosYMinMax().getSecond()+ 1;;
        double vMin = 0;
        double vMax = 1;
        var scaler = ScalerLinear.of(vMin, vMax, 0.0, 1.0);
        Map<ActionGrid, double[][]> aGrids = new HashMap<>();
        informer.getValidActions().forEach(ay -> {
            aGrids.put(ay, emptyGrid(nRows, nCol));
        });
        double[][] vGrid = emptyGrid(nRows, nCol);
        Object[][] policyGrid = new Object[nRows][nCol];
        for (int x = 0; x < nCol; x++) {
            for (int y = 0; y < nRows; y++) {
                var s = StateGrid.of(x, y);
                double value = agent.read(s);
                vGrid[y][x] = scale(scaler, value);
                policyGrid[nRows - 1 - y][x] = agent.chooseActionNoExploration(s).toString();
                for (ActionGrid a : informer.getValidActions()) {
                    double av = agent.read(s, a);
                    aGrids.get(a)[y][x] = scale(scaler, av);
                }
            }
        }

        List<double[][]> grids = new ArrayList<>();
//        informer.getValidActions().forEach(a -> grids.add(GridFactory.toSeries(aGrids.get(a))));
        grids.add(GridFactory.toSeries(vGrid));
        var dto = GraphicsDto.builder()
                .grids(grids)
                .tableData(Collections.singletonList(policyGrid))
                .animationDelay((int) delayFunction.applyAsDouble(ei))
                .build();
        kitEpisode.postAndSleep(dto);
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

    private static GfxComponentFactory episodeGfx(AnimationSettings as, EnvironmentSplittingPath env) {
        var informer = env.informer();
        var factory = GfxComponentFactory.of(as);
        factory.addHeatMap("Value");
//        factory.addTable(informer.getPosXMinMax().getSecond(), true);
        return factory;
    }


    private static AnimationSettings createSetting(AnimationConfig cfg) {
        return AnimationSettings.builder()
                .frameWidth(WIDTH).frameHeight(HEIGHT)
                .frameXLocation(FRAME_X_LOCATION).frameYLocation(200)
                .panelWidth(WIDTH).panelHeight(HEIGHT)
                .tableWidth(WIDTH).tableHeight(HEIGHT / 2)
                .order(List.of(Step.LINE, Step.HEATMAP, Step.TABLE))
                .margin(0)
                .ndigits(cfg.ndigits())
                .fontsize(cfg.fontsize())
                .fontsizeAxis(cfg.fontsizeAxis())
                .build();
    }

    private static double scale(ScalerLinear scaler, double value) {
        return scaler.calcOutDouble(MathUtil.clip(value, scaler.d0, scaler.d1));
    }


    private static double[][] emptyGrid(Integer nRows, Integer nCol) {
        return new double[nRows][nCol];
    }

}
