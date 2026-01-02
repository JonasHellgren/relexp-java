package chapters.ch10.bandit.domain.agent;

public record AgentParametersBandit(double[] z_array_init) {

    public static AgentParametersBandit of(double[] z_array_init) {
        return new AgentParametersBandit(z_array_init);
    }

}
