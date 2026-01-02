package chapters.ch10.bandit.factory;

import chapters.ch10.bandit.domain.agent.AgentParametersBandit;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class FactoryAgentParametersBandit {

    public AgentParametersBandit highLeftProbability() {
        return getBandit(3, 1);
    }

    public AgentParametersBandit equalProbability() {
        return getBandit(1, 1);
    }

    public AgentParametersBandit highRightProbability() {
        return getBandit(1, 3);
    }

    @NotNull
    private static AgentParametersBandit getBandit(double z0, double z1) {
        return AgentParametersBandit.of(new double[]{z0, z1});
    }

}
