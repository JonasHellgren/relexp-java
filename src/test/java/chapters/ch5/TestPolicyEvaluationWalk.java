package chapters.ch5;

import chapters.ch5.domain.policy_evaluator.StatePolicyEvaluationMc;
import chapters.ch5.factory.StatePolicyEvaluationFactory;
import chapters.ch5.implem.walk.StateWalk;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;

public class TestPolicyEvaluationWalk {

    public static final double TOL = 1e-1;
    StatePolicyEvaluationMc evaluator;

    @BeforeEach
    void init() {
        evaluator = StatePolicyEvaluationFactory.createWalk(StatePolicyEvaluationMc.DEFAULT_SETTINGS);
    }

    @Test
    void whenEvaluating_thenCorrect() {

        evaluator.evaluate();
        var memory = evaluator.getMemory();
        System.out.println(memory);

        assertAll(
                () -> Assertions.assertEquals(-0.5, memory.read(StateWalk.of(2)), TOL),
                () -> Assertions.assertEquals(0.0, memory.read(StateWalk.of(3)), TOL),
                () -> Assertions.assertEquals(0.5, memory.read(StateWalk.of(4)), TOL)
        );

    }

}
