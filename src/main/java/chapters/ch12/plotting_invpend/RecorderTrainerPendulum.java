package chapters.ch12.plotting_invpend;

import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import chapters.ch12.domain.inv_pendulum.trainer.core.TrainerDependencies;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecorderTrainerPendulum {

    private List<MeasuresPendulumTraining> measuresList;

    public static RecorderTrainerPendulum empty() {
        return new RecorderTrainerPendulum(new ArrayList<>());
    }

    public void add(MeasuresPendulumTraining measures) {
        measuresList.add(measures);
    }

    public boolean isEmpty() {
        return measuresList.isEmpty();
    }

    public List<Double> trajectory(MeasurePendulum measure) {
        return measuresList.stream().map(measure.mapFunction).toList();
    }

    public void addRecord(double ei, double sumRewards, StatePendulum s, TrainerDependencies dependencies) {
        var agent = dependencies.agent();
        var state0 = StatePendulum.ofStart(0, 0);
        var avList = agent.read(state0);
        var measures = MeasuresPendulumTraining.builder()
                .episode(ei)
                .sumRewards(sumRewards)
                .time(s.nSteps()*dependencies.environment().getParameters().dt()).nSteps(s.nSteps())
                .loss(agent.loss())
                .q0ccw(avList.get(0).actionValue())
                .q0n(avList.get(1).actionValue())
                .q0cw(avList.get(2).actionValue())
                .build();
        add(measures);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        measuresList.forEach(m -> sb.append(m.toString()).append("\n"));
        return sb.toString();
    }

}
