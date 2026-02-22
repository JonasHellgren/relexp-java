package chapters.ch4.implem_animation;

import chapters.ch4.domain.animation.AnimationGridI;
import core.animation.*;
import core.gridrl.AgentGridI;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import oshi.util.FormatUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnimationTreasure implements AnimationGridI {

    static final int HEIGHT = 300;
    static final int WIDTH = 300;
    static final int N_COLUMNS = 2;
    static final int N_DIGITS = 2;

    static final IntervalData ANIMATIONS_SLEEP = IntervalData.of(
            List.of(0.0, 10.0, 99990.0),  //cuts
            List.of(1000.0, 1.0, 1000.0)   //animation time delays
    );

    AnimationKit kitStep, kitEpisode;
    DoubleUnaryOperator delayFunction;

    public static AnimationTreasure empty() {
        return new AnimationTreasure(
                AnimationKit.empty(),
                AnimationKit.empty(),
                DelayIntervalFunction.from(IntervalData.empty()));
    }

    public static AnimationTreasure create(EnvironmentGridI env) {
        var asStep = createSetting();
        var asEpisode = createSetting()
                .withFrameXLocation(WIDTH * 2).withFrameHeight(HEIGHT * 2)
                .withTableWidth((int) (WIDTH * 0.75)).withTableHeight(HEIGHT / 2);
        return new AnimationTreasure(
                AnimationKit.of(environmentGfx(asStep,env), asStep),
                AnimationKit.of(episodeGfx(asEpisode), asEpisode),
                DelayIntervalFunction.from(ANIMATIONS_SLEEP));
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
        addSeekerLines(s,lines);
        //   addFixedObjectsLines(s, lines);
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

    @Override
    public void postEpisode(AgentGridI agent, EnvironmentGridI env0) {

    }


    private static void addSeekerLines(StateGrid s, List<LineSegment> lines) {
        double x0 = s.x();
        double y0 = s.y();
        lines.add(LineSegment.blackBold(x0, y0, x0, y0));

    }

    private static GfxComponentFactory environmentGfx(AnimationSettings as, EnvironmentGridI env) {
        var factory = GfxComponentFactory.of(as);
        var posxMinMax= env.informer().getPosXMinMax();
        var posyMinMax= env.informer().getPosYMinMax();
        factory.addLineChart("", "x", -1, posxMinMax.getSecond()+1, "y", -1, posyMinMax.getSecond()+1);
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
