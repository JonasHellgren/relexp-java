package chapters.ch9.animation;

import chapters.ch10.bandit.domain.agent.MemoryBandit;
import chapters.ch10.bandit.domain.trainer.ExperienceBandit;
import core.animation.*;
import core.foundation.config.AnimationConfig;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.collections.MyMatrixArrayUtil;
import core.foundation.util.formatting.NumberFormatterUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimationBandit implements  AnimationPolicyI {

    static final int HEIGHT_ENV = 200;
    public static final int TABLE_HEIGHT = (int) (HEIGHT_ENV * 0.5);
    static final int HEIGHT_VAL = 200;
    static final int WIDTH = 300;
    static final int X_LOCATION_ENV = 100;
    static final int X_LOCATION_VAL = 500;
    static final int N_COLUMNS = 2;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 5.0, 100.0),  //cuts
            List.of(10000.0, 1.0, 500.0)   //animation time delays
    );
    public static final int Z_MAX = 5;

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;
    AnimationConfig cfg;

    public static AnimationBandit create(AnimationConfig cfg) {
        var asStep = AnimationSettings.of(cfg,WIDTH,HEIGHT_ENV, TABLE_HEIGHT, X_LOCATION_ENV);
        var asEpisode = AnimationSettings.of(cfg,WIDTH, HEIGHT_VAL, TABLE_HEIGHT, X_LOCATION_VAL);
        return new AnimationBandit(
                AnimationKit.of(environmentGfx(asStep), asStep),
                AnimationKit.of(episodeGfx(asEpisode), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP),
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
    public void postStep(int ei, int eiMax, List<ExperienceBandit> experiences) {
        if (isEmpty()) return;
        List<LineSegment> lines = new ArrayList<>();
     //   addSeekerLines(s, lines);
        var lineData = List.of(lines);
        double reward=experiences.get(0).reward();
        String actionLorR = experiences.get(0).action().toString();
        String isCoin = experiences.get(0).stepReturn().isCoin()? "yes" : "no";
        var tableData = Collections.singletonList(new Object[][]{
                {"episode", ei+"("+eiMax+")"},
                {"action", actionLorR},
                {"isCoin", isCoin},
                {"reward", reward},
        });
        var dto = GraphicsDto.dtoStep(
                lineData, tableData, 0, false);
        kitStep.postAndSleep(dto);
    }

    @Override
    public void postEpisode(MemoryBandit memory,int ei, double returnAtT, double[] gradLog, double[] probArray) {
        if (isEmpty()) return;
        int nRows = 1;
        int nCols = 2;
        double[][] vGrid = MyMatrixArrayUtil.emptyMatrix(nRows,nCols);
        var z=memory.getMemoryParameters();
        var scaler = ScalerLinear.of(-Z_MAX, Z_MAX, 0.0, 1.0);
        vGrid[0][0]=scaler.calcOutDouble(z[0]);
        vGrid[0][1]=scaler.calcOutDouble(z[1]);
        List<double[][]> grids = new ArrayList<>();
        grids.add(GridFactory.toSeries(vGrid));

        var tableData = Collections.singletonList(new Object[][]{
                {"z", "("+ round(z[0]) +","+round(z[1])+")"},
                {"return", round(returnAtT)},
                {"grad log", "("+ round(gradLog[0]) +","+round(gradLog[1])+")"},
                {"probability (L,R)", "("+ round(probArray[0]) +","+round(probArray[1])+")"},

        });

        kitEpisode.postAndSleep(GraphicsDto.dtoEpisode(grids,tableData, (int) delayFunction.applyAsDouble(ei)));
    }

    private static double round(double z) {
        return NumberFormatterUtil.roundTo1Decimals(z);
    }

    private static GfxComponentFactory environmentGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addLineChart("",
                "x", Pair.create(0, 10),
                "y", Pair.create(1, 10));
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static GfxComponentFactory episodeGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addHeatMap("Value");
        factory.addTable(N_COLUMNS, false);
        return factory;
    }


}
