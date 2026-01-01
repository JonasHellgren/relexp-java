package chapters.ch5.domain.memory;


import chapters.ch5.domain.environment.ActionMcI;
import chapters.ch5.domain.environment.StateMcI;

public interface StateActionMemoryMcI {
    double read(StateMcI state, ActionMcI action);
    void write(StateMcI state, ActionMcI action, double value);
    boolean isEmpty();
    boolean isPresent(StateActionMcI stateAction);
    int size();
}
