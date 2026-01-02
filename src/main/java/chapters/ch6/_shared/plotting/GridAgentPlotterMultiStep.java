package chapters.ch6._shared.plotting;


import chapters.ch6.domain.agent.core.AgentGridMultiStepI;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.gridrl.StateGrid;
import core.plotting.chart_plotting.GridAgentPlotter;

/***
 * Reusing code, but with multi-step agent
 */

public class GridAgentPlotterMultiStep extends GridAgentPlotter {

    AgentGridMultiStepI agentMultiStep;

    public static GridAgentPlotterMultiStep of(TrainerDependenciesMultiStep dependencies,
                                               String fileNameAddO, int nofDigits) {
        return new GridAgentPlotterMultiStep(dependencies, fileNameAddO, nofDigits);
    }


    public GridAgentPlotterMultiStep (TrainerDependenciesMultiStep dependencies,
                                      String fileNameAddO, int nofDigits) {
        super(dependencies.environment(), null, fileNameAddO, nofDigits);
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
