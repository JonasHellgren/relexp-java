package chapters.ch4.implem_animation;

import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import core.animation.*;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.math.MathUtil;
import core.gridrl.ActionGrid;
import core.gridrl.AgentGridI;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;
import oshi.util.FormatUtil;
import java.util.*;

/**
 * Defines the animation for the road environment
 *
 * stepGfx is the left frame, showing car moving
 * episodeGfx, is the right frame, showing the agent memory
 */

public record AnimationRoad(AnimationKit kitStep, AnimationKit kitEpisode) {

    public static final int HEIGHT = 300;
    public static final int WIDTH = 300;
    public static final int N_COLUMNS = 2;

    public static AnimationRoad empty() {
        return new AnimationRoad(AnimationKit.empty(), AnimationKit.empty());
    }

    public static AnimationRoad create() {
        var asStep = createSetting();
        var asEpisode = createSetting()
                .withFrameXLocation(WIDTH * 2).withFrameHeight(HEIGHT * 2)
                .withTableWidth((int) (WIDTH*0.75)).withTableHeight(HEIGHT/2);
        return new AnimationRoad(
                AnimationKit.of(stepGfx(asStep), asStep),
                AnimationKit.of(episodeGfx(asEpisode), asEpisode));
    }

    private static GfxComponentFactory stepGfx(AnimationSettings as) {
        var factory = GfxComponentFactory.of(as);
        factory.addLineChart("", "x", 0, 4, "y", -1, 2);
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
        kitStep.start();
        kitEpisode.start();
    }

    public void postStep(StateGrid s, int ei, int eiMax, double pRand, double reward) {
        double xShift = -0.15;
        double widthCar = 0.3;
        double widthbull = 0.1;
        var car = LineSegment.blackBold(s.x()+xShift, s.y(), s.x() + widthCar +xShift, s.y());
        var bull = LineSegment.redBold(3, 1, 3 + widthbull, 1);
        var lineData = List.of(List.of(car, bull));
        var tableData = Collections.singletonList(new Object[][]{
                {"episode", String.valueOf(ei)},
                {"number of episodes", String.valueOf(eiMax)},
                {"reward", round(reward)},
                {"probability random action", round(pRand)}
        });
        var dto = GraphicsDto.builder()
                .lines(lineData)
                .tableData(tableData)
                .isFail(reward < -99)
                .animationDelay(100)
                .build();
        kitStep.postAndSleep(dto);
    }

    public void postEpisode(AgentGridI agent, EnvironmentGridI env0) {
        var env=(EnvironmentRoad)env0;
        var ep = env.getParameters();
        Integer nCol = ep.posXMinMax().getSecond();
        Integer nRows = ep.posYMinMax().getSecond()+1;
        double vMin = -5;
        var scaler= ScalerLinear.of(vMin,0.0,0.0,1.0);
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
                policyGrid[nRows-1-y][x] = agent.chooseActionNoExploration(s).toString();
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
                .animationDelay(10000)
                .build();
        kitEpisode.postAndSleep(dto);
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
                .tableWidth(WIDTH).tableHeight(HEIGHT/2)
                .order(List.of(Step.LINE, Step.HEATMAP, Step.TABLE))
                .margin(0)
                .build();
    }

    private static float round(double pRand) {
        return FormatUtil.round((float) pRand, N_COLUMNS);
    }


}