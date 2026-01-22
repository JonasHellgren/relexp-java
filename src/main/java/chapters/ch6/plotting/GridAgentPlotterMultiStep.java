package chapters.ch6.plotting;


import chapters.ch6.domain.agent.AgentGridMultiStepI;
import chapters.ch6.domain.trainer_dep.core.TrainerDependenciesMultiStep;
import core.foundation.config.PlotConfig;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.gridrl.StateGrid;
import core.plotting_rl.chart.GridAgentPlotter;

/***
 * Reusing code, but with multi-step agent
 */

public class GridAgentPlotterMultiStep extends GridAgentPlotter {

    AgentGridMultiStepI agentMultiStep;

    public static GridAgentPlotterMultiStep of(TrainerDependenciesMultiStep dependencies,
                                               int nofDigits, PlotConfig plotCfg,
                                               String path) {
        return new GridAgentPlotterMultiStep(dependencies,  nofDigits,plotCfg,path);
    }


    public GridAgentPlotterMultiStep (TrainerDependenciesMultiStep dependencies,
                                      int nofDigits, PlotConfig plotCfg, String path) {
        super(dependencies.environment(), null, nofDigits,plotCfg,path);
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
