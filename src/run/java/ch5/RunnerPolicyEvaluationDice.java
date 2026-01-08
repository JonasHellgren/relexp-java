package ch5;

import chapters.ch5.domain.policy_evaluator.StateActionPolicyEvaluationMc;
import chapters.ch5.factory.StateActionPolicyEvaluationFactory;
import chapters.ch5.plotting.ValueMemoryMcPlotter;
import chapters.ch5.domain.memory.StateActionMemoryMcI;
import chapters.ch5.implem.dice.ActionDice;
import chapters.ch5.implem.dice.EnvironmentDice;
import chapters.ch5.implem.dice.StateDice;
import chapters.ch5.implem.dice.StateMemoryDice;
import core.foundation.gadget.timer.CpuTimer;
import core.foundation.util.math.MovingAverage;
import core.plotting.chart_plotting.ChartSaverAndPlotter;
import core.plotting_rl.chart.ManyLinesFactory;
import core.plotting.plotting_2d.ManyLinesChartCreator;

import java.io.IOException;
import java.util.List;

/***
 * Finds and present dice game policy
 * when state is (4,1) then following predicts consequence of throwing dice:
 * 4+-2/6*4+4/6*(1/4+2/4+3/4+4/4)=4.3
 */

public class RunnerPolicyEvaluationDice {

    public static final int SPACE_BETWEEN_X_TICKS = 2500;
    public static final int LENGTH_WINDOW = 2500;

    public static void main(String[] args) throws IOException {
        var timer= CpuTimer.empty();
        var policyEvaluation = StateActionPolicyEvaluationFactory.createDice();
        policyEvaluation.evaluate();
        timer.printInMs();
        saveAndPlotMemory(policyEvaluation);
        saveAndPlotTdError(policyEvaluation);
    }

    private static void saveAndPlotTdError(StateActionPolicyEvaluationMc policyEvaluation) throws IOException {
        var errors=getFilteredList(policyEvaluation.getErrorList());
        var creator = getChartCreator(errors);
        creator.addLine("Monte Carlo",errors);
        var chart = creator.create();
        var styler=chart.getStyler();
        styler.setLegendVisible(false);
        ChartSaverAndPlotter.showChartSaveInFolderMonteCarlo(chart, "error-vs-fit-dice");
    }

    private static void saveAndPlotMemory(StateActionPolicyEvaluationMc policyEvaluation) {
        var memoryPolicy = policyEvaluation.getMemory();
        var stateMemoryT = getStateMemoryDice(memoryPolicy, ActionDice.T);
        var stateMemoryS = getStateMemoryDice(memoryPolicy, ActionDice.S);
        saveAndPlot(stateMemoryT, "Dice"+ActionDice.T);
        saveAndPlot(stateMemoryS, "Dice"+ActionDice.S);
    }

    private static void saveAndPlot(StateMemoryDice stateMemoryT, String dice) {
        var plotter = ValueMemoryMcPlotter.of(
                stateMemoryT,
                ValueMemoryMcPlotter.Settings.builder()
                        .nofDigits(1)
                        .yLabel("Count").xLabel("Score")
                        .fileNameAddOn(dice)
                        .envName(EnvironmentDice.NAME)
                        .nRows(2).nCols(7)
                        .startRow(0).startColumn(0)
                        .build());
        plotter.plotStateValues();
    }

    private static StateMemoryDice getStateMemoryDice(StateActionMemoryMcI memoryPolicy, ActionDice actionDice) {
        var stateMemoryT = StateMemoryDice.create();
        for (int count = 0; count < 2; count++) {
            for (int sum = 0; sum < 7; sum++) {
                var s = StateDice.of(sum, count);
                double value = memoryPolicy.read(s, actionDice);
                if (s.isValid()) {
                    stateMemoryT.write(s, value);
                }
            }
        }
        return stateMemoryT;
    }

    private static ManyLinesChartCreator getChartCreator(List<Double> errorListMc) throws IOException {
        var factory = ManyLinesFactory.builder()
                .spaceBetweenXTicks(SPACE_BETWEEN_X_TICKS)
                .nItems(errorListMc.size())
                .xLabel("Fit")
                .yLabel("Error")
                .build();
        return factory.getManyLinesChartCreator();
    }

    private static List<Double> getFilteredList(List<Double> errorList0) {
        var movingAverage = new MovingAverage(LENGTH_WINDOW, errorList0);
        return movingAverage.getFiltered();
    }


}
