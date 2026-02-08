package chapters.ch4.implem_animation;

import chapters.ch4.implem.blocked_road_lane.core.EnvironmentParametersRoad;
import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import core.animation.*;
import core.foundation.gadget.math.ScalerLinear;
import core.foundation.util.math.MathUtil;
import core.gridrl.ActionGrid;
import core.gridrl.AgentGridI;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;
import org.jetbrains.annotations.NotNull;
import oshi.util.FormatUtil;

import java.util.*;

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
        double yShift = 0.0;
        var car = LineSegment.blackBold(s.x(), s.y() + yShift, s.x() + 1, s.y() + yShift);
        var bull = LineSegment.redBold(3 + 0.5, 1 + yShift, 3 + 1 + 0.5, 1 + yShift);
        var lineData = List.of(List.of(car, bull));
        var tableData = Collections.singletonList(new Object[][]{
                {"episode", String.valueOf(ei)},
                {"max episode", String.valueOf(eiMax)},
                {"reward", round(reward)},
                {"probability random action", round(pRand)}
        });
        var dto = GraphicsDto.builder()
                .lines(lineData)
                .tableData(tableData)
                .isFail(reward < -100)
                .build();
        kitStep.postAndSleep(dto);
    }

    public void postEpisode(AgentGridI agent, EnvironmentGridI env0) {
        var env=(EnvironmentRoad)env0;
        var ep = env.getParameters();
        Integer nCol = ep.posXMinMax().getSecond();
        Integer nRows = ep.posYMinMax().getSecond()+1;
        double vMin = -5; //ep.rewardAtFailPos().mean();
        var scaler= ScalerLinear.of(vMin,0.0,0.0,1.0);

        Map<ActionGrid, double[][]> aGrids = new HashMap<>();
        ep.validActions().forEach(ay -> {
            aGrids.put(ay, emptyGrid(nRows, nCol));
        });
        double[][] vGrid = emptyGrid(nRows, nCol);
        Object[][][] policyGrid = new Object[nRows][nCol][1];
       // Object[][][] policyGrid = new Object[nRows][nCol][1];
       // Object[][][] policyGrid = new Object[nCol][nCol][1];

        for (int x = 0; x < nCol; x++) {
            for (int y = 0; y < nRows; y++) {
                var s = StateGrid.of(x, y);

                double value = agent.readValue(s);
                vGrid[y][x] = scale(scaler, value, vMin);
             //   policyGrid[0][x][y] = agent.chooseActionNoExploration(s).toString();
                for (ActionGrid a : ep.validActions()) {
                    double av = agent.read(s, a);
                    aGrids.get(a)[y][x] = scale(scaler, av, vMin);
                }



            }
        }

        for (int y = 0; y < nRows; y++) {
           // policyGrid[0][y] =new Object[]{"a","b","c",String.valueOf(y)};

            String[] txtrow = new String[nCol];
            for (int x = 0; x < nCol; x++) {
                var s = StateGrid.of(x, y);
                txtrow[x] = agent.chooseActionNoExploration(s).toString();
            }


            policyGrid[0][nRows-y-1] =  txtrow; //new Object[]{"a","b","c",String.valueOf(y)};

        }



      //  policyGrid[0][0] =new Object[]{"a","b","c","d"};
  //      policyGrid[0][1] =new Object[]{"aa","bb","c","d"};

        System.out.println("policyGrid = " + Arrays.deepToString(policyGrid));




        //   vars gridN=GridFactory.toSeries(getGridN());
        List<double[][]> grids = new ArrayList<>();
        ep.validActions().forEach(a -> grids.add(GridFactory.toSeries(aGrids.get(a))));
        grids.add(GridFactory.toSeries(vGrid));
     //   var dto = GraphicsDto.grids(grids, false);

        var dto = GraphicsDto.builder()
                .grids(grids)
                .tableData(List.of(policyGrid))
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
                .animationDelay(100)
                .build();
    }

    private static float round(double pRand) {
        return FormatUtil.round((float) pRand, N_COLUMNS);
    }


}