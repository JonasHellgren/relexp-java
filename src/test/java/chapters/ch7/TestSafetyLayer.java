package chapters.ch7;

import chapters.ch7.domain.safety_layer.SafetyLayer;
import chapters.ch7.factory.SafetyLayerFactoryTreasure;
import chapters.ch7.factory.TrainerDependencySafeFactory;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestSafetyLayer {

    SafetyLayer safetyLayer;

    @BeforeEach
    void setup() {
        var dependencies = TrainerDependencySafeFactory.treasureForTest();
        safetyLayer = SafetyLayerFactoryTreasure.produce(dependencies);
    }

    @Test
    void testMemorySize() {
        assertEquals(9, safetyLayer.memorySize());
    }

    @Test
    void givenStatex0y1_thenNoCorrection() {
        var state = StateGrid.of(0, 1);
        var action = ActionGrid.random();
        var aC = safetyLayer.correct(state, action);
        assertEquals(action, aC);
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,E, false",   //s,a -> is corrected
            "1,1,N, true",
            "1,1,S, true",
            "1,1,W, false",
    })
    void givenStateX1y1_thenCorrect(ArgumentsAccessor arguments) {
        assertCorrection(arguments);
    }

    @Test
    void givenStateX1y1_whenActionN_thenRandomCorrected() {
        var state = StateGrid.of(1, 1);
        var action = ActionGrid.N;
        Set<ActionGrid> actions = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            var aC = safetyLayer.correct(state, action);
            actions.add(aC);
        }
        assertEquals(2, actions.size());
        assertTrue(actions.containsAll(Set.of(ActionGrid.E, ActionGrid.W)));
    }


    @ParameterizedTest
    @CsvSource({
            "6,3,E, true",   //s,a -> is corrected
            "6,3,N, false",
            "6,3,S, false",
            "6,3,W, false",
    })
    void givenStateX6y3_thenCorrect(ArgumentsAccessor arguments) {
        assertCorrection(arguments);
    }

    @ParameterizedTest
    @CsvSource({
            "5,1,E, false",
            "5,1,N, false",
            "5,1,S, true",
            "5,3,W, false",
            "6,1,E, false",
            "6,1,N, false",
            "6,1,S, true",
            "6,3,W, false",
            "7,1,E, false",
            "7,1,N, false",
            "7,1,S, true",
            "7,3,W, false",
            "8,1,E, false",
            "8,1,N, false",
            "8,1,S, true",
            "8,3,W, false",
    })
    void givenStateX5toy1_thenCorrect(ArgumentsAccessor arguments) {
        assertCorrection(arguments);
    }

    private void assertCorrection(ArgumentsAccessor arguments) {
        var decoder = TestArgumentsDecoderSafe.of(arguments);
        var aC = safetyLayer.correct(decoder.state(), decoder.action());
        boolean isCorrected = !aC.equals(decoder.action());
        Assertions.assertEquals(decoder.isCorrected(), isCorrected);
    }


}
