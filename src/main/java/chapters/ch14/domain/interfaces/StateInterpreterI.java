package chapters.ch14.domain.interfaces;

public interface StateInterpreterI<SI,S> {
    SI interpret(S s);
}
