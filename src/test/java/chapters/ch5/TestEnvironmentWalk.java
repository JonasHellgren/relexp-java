package chapters.ch5;

import chapters.ch5._shared.factory.GridParametersWalkFactory;
import chapters.ch5.domain.environment.StepReturnMc;
import chapters.ch5.implem.walk.EnvironmentWalk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestEnvironmentWalk {

    EnvironmentWalk environment;

    @BeforeEach
    void init() {
        environment = EnvironmentWalk.of(GridParametersWalkFactory.produce());
    }

    @ParameterizedTest
    @CsvSource({
            "2,W,  1,-1.0,true,true",   //x,a -> x',r,isPark, isTerminal
            "2,E,  3,0.0,false,false",
            "3,W,  2,0.0,false,false",
            "3,E,  4,0.0,false,false",
            "4,W,  3,0.0,false,false",
            "4,E,  5,1.0,false,true",
    })
    void givenPos01_whenStepping_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ParamMcTestDecoder.ofWalk(arguments);
        var sr = environment.step(decoder.state(), decoder.action());
        assertStepReturn(decoder, sr);
    }


    private static void assertStepReturn(ParamMcTestDecoder decoder, StepReturnMc sr) {
        assertEquals(decoder.sNext(), sr.sNext());
        assertEquals(decoder.reward(), sr.reward());
        assertEquals(decoder.isFail(), sr.isFail());
        assertEquals(decoder.isTerminal(), sr.isTerminal());
    }


}
