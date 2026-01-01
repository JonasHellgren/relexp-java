package chapters.ch5.domain.memory;


import chapters.ch5.domain.environment.StateMcI;

public interface StateMemoryMcI {
    double read(StateMcI state);
    void write(StateMcI state, double value);
    boolean isEmpty();
    boolean isPresent(StateMcI state);
    int size();
}
