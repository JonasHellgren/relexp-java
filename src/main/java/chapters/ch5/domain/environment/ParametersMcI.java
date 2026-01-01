package chapters.ch5.domain.environment;

public interface ParametersMcI {
    boolean isTerminalNonFail(StateMcI state);
    boolean isFail(StateMcI state);
}
