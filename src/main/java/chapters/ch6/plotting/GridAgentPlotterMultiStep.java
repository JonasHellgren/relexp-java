package chapters.ch6.plotting;


import chapters.ch6.domain.agent.AgentGridMultiStepI;
import chapters.ch6.domain.trainer_dep.core.TrainerDependenciesMultiStep;
import core.foundation.config.PlotConfig;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.gridrl.StateGrid;
import core.plotting_rl.chart.GridAgentPlotterOld;

/***
 * Reusing code, but with multi-step agent
 */

public class GridAgentPlotterMultiStep extends GridAgentPlotterOld {

    AgentGridMultiStepI agentMultiStep;

    public static GridAgentPlotterMultiStep of(TrainerDependenciesMultiStep dependencies,
                                               String fileNameAddO, int nofDigits, PlotConfig plotCfg) {
        return new GridAgentPlotterMultiStep(dependencies, fileNameAddO, nofDigits,plotCfg);
    }


    public GridAgentPlotterMultiStep (TrainerDependenciesMultiStep dependencies,
                                      String fileNameAddO, int nofDigits, PlotConfig plotCfg) {
        super(dependencies.environment(), null, fileNameAddO, nofDigits,plotCfg);
        this.agentMultiStep =  dependencies.agent();
    }


    @Override
    public String getValueInStateAsString(StateGrid state) {
        return NumberFormatterUtil.getRoundedNumberAsString(
                agentMultiStep.read(state), super.getNofDigits());
    }

    @Override
    public String getActionAsString(StateGrid state) {
        return agentMultiStep.chooseActionNoExploration(state).toString();
    }


}
