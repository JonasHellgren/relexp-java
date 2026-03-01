package chapters.ch4.implem_animation;

import chapters.ch4.domain.animation.AnimationGridI;
import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import core.animation.*;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.math.MathUtil;
import core.gridrl.ActionGrid;
import core.gridrl.AgentGridI;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;
import oshi.util.FormatUtil;

import java.util.*;
import java.util.function.DoubleUnaryOperator;

/**
 * Defines the animation for the road environment
 * <p>
 * stepGfx is the left frame, showing car moving
 * episodeGfx, is the right frame, showing the agent memory
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimationRoad implements AnimationGridI {

    static final int HEIGHT = 300;
    static final int WIDTH = 300;
    static final int N_COLUMNS = 2;
    static final int N_DIGITS = 2;
    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 10.0, 995.0),  //cuts
            List.of(2000.0, 1.0, 2000.0)   //animation time delays
    );

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;

    public static AnimationRoad empty() {
        return new AnimationRoad(
                AnimationKit.empty(),
                AnimationKit.empty(),
                DelayIntervalFunction.from(IntervalData.empty()));
    }

    public static AnimationRoad create() {
        var asStep = createSetting();
        var asEpisode = createSetting()
                .withFrameXLocation(WIDTH * 2).withFrameHeight(HEIGHT * 2)
                .withTableWidth((int) (WIDTH * 0.75)).withTableHeight(HEIGHT / 2);
        return new AnimationRoad(
                AnimationKit.of(environmentGfx(asStep), asStep),
                AnimationKit.of(episodeGfx(asEpisode), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP));
    }


    public boolean isEmpty() {
        return kitEpisode.isEmpty();
    }

    private static GfxComponentFactory environmentGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addLineChart("",
                "x",  Pair.create(0, 4),
                "y",  Pair.create(-1, 2));
        factory.addTable(N_COLUMNS, false);
        return factory;
    }

    private static GfxComponentFactory episodeGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addHeatMap("N");
        factory.addHeatMap("E");
        factory.addHeatMap("S");
        factory.addHeatMap("Value");
        factory.addTable(3, true);
        return factory;
    }


    public void start() {
        if (isEmpty()) return;
        kitStep.start();
        kitEpisode.start();
    }

    public void postStep(StateGrid s, int ei, int eiMax, double pRand, double reward) {
        if (isEmpty()) return;
        List<LineSegment> lines = new ArrayList<>();
        addCarlines(s, lines);
        addBullLines(lines);
        var lineData = List.of(lines);
        var tableData = Collections.singletonList(new Object[][]{
                {"episode", String.valueOf(ei)},
                {"number of episodes", String.valueOf(eiMax)},
                {"reward", round(reward)},
                {"probability random action", round(pRand)},
                {"x pos", s.x()},
                {"y pos", s.y()}
        });
        var dto = GraphicsDto.builder()
                .lines(lineData)
                .tableData(tableData)
                .isFail(reward < -99)
                .animationDelay((int) delayFunction.applyAsDouble(ei))
                .build();
        kitStep.postAndSleep(dto);
    }

    public void postEpisode(AgentGridI agent, EnvironmentGridI env0) {
        if (isEmpty()) return;

        var env = (EnvironmentRoad) env0;
        var ep = env.getParameters();
        int nCol = ep.posXMinMax().getSecond();
        int nRows = ep.posYMinMax().getSecond() + 1;
        double vMin = -5;
        var scaler = ScalerLinear.of(vMin, 0.0, 0.0, 1.0);
        Map<ActionGrid, double[][]> aGrids = new HashMap<>();
        ep.validActions().forEach(ay -> {
            aGrids.put(ay, emptyGrid(nRows, nCol));
        });
        double[][] vGrid = emptyGrid(nRows, nCol);
        Object[][] policyGrid = new Object[nRows][nCol];
        for (int x = 0; x < nCol; x++) {
            for (int y = 0; y < nRows; y++) {
                var s = StateGrid.of(x, y);
                double value = agent.readValue(s);
                vGrid[y][x] = scale(scaler, value, vMin);
                policyGrid[nRows - 1 - y][x] = agent.chooseActionNoExploration(s).toString();
                for (ActionGrid a : ep.validActions()) {
                    double av = agent.read(s, a);
                    aGrids.get(a)[y][x] = scale(scaler, av, vMin);
                }
            }
        }

        List<double[][]> grids = new ArrayList<>();
        ep.validActions().forEach(a -> grids.add(GridFactory.toSeries(aGrids.get(a))));
        grids.add(GridFactory.toSeries(vGrid));
        var dto = GraphicsDto.builder()
                .grids(grids)
                .tableData(Collections.singletonList(policyGrid))
                .animationDelay(0)
                .build();
        kitEpisode.postAndSleep(dto);
    }


    private void addCarlines(StateGrid s, List<LineSegment> lines) {
        double widthCar = 1.0;
        double heightCar = 0.5;
        double xShift = -widthCar / 2;
        double x0 = s.x() + xShift;
        double y0 = s.y() - heightCar / 2;
        double x1 = x0 + widthCar;
        double y1 = y0 + heightCar;
        double xrearWind = x0 + (x1 - x0) * 0.1;
        double xfrontWind1 = x0 + (x1 - x0) * 0.55;
        double xfrontWind2 = x0 + (x1 - x0) * 0.7;
        lines.add(LineSegment.black(x0, y0, x0, y1));
        lines.add(LineSegment.black(x0, y1, x0 + widthCar / 2.0, y1));
        lines.add(LineSegment.black(x0 + widthCar / 2.0, y1, x1, y1));
        lines.add(LineSegment.black(x1, y1, x1, y0));
        lines.add(LineSegment.black(x1, y0, x0 + widthCar / 2.0, y0));
        lines.add(LineSegment.black(x0 + widthCar / 2.0, y0, x0, y0));
        lines.add(LineSegment.black(xrearWind, y1, xrearWind, y0));
        lines.add(LineSegment.black(xfrontWind1, y1, xfrontWind1, y0));
        lines.add(LineSegment.black(xfrontWind2, y1, xfrontWind2, y0));
    }


    private static void addBullLines(List<LineSegment> lines) {
        double widthbull = 0.1;
        var bull = LineSegment.redBold(3, 1, 3 + widthbull, 1);
        lines.add(bull);
    }

    private static double scale(ScalerLinear scaler, double value, double vMin) {
        return scaler.calcOutDouble(MathUtil.clip(value, vMin, 0.0));
    }

    private static double[][] emptyGrid(Integer nRows, Integer nCol) {
        return new double[nRows][nCol];
    }

    private static AnimationSettings createSetting() {
        return AnimationSettings.builder()
                .frameWidth(WIDTH).frameHeight(HEIGHT)
                .frameXLocation(100).frameYLocation(200)
                .panelWidth(WIDTH).panelHeight(HEIGHT)
                .tableWidth(WIDTH).tableHeight(HEIGHT / 2)
                .order(List.of(Step.LINE, Step.HEATMAP, Step.TABLE))
                .margin(0)
                .build();
    }

    private static float round(double pRand) {
        return FormatUtil.round((float) pRand, N_DIGITS);
    }


}