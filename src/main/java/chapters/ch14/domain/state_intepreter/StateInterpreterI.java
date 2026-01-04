package chapters.ch14.domain.state_intepreter;

public interface StateInterpreterI<SI,S> {
    SI interpret(S s);
}
