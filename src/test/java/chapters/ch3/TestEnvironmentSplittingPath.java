package chapters.ch3;

import chapters.ch3.factory.EnvironmentParametersSplittingFactory;
import chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath;
import core.gridrl.StateGrid;
import core.gridrl.StepReturnGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestEnvironmentSplittingPath {

    EnvironmentSplittingPath environment;
    StateGrid state01, state21, state42, state40;

     @BeforeEach
      void init() {
         environment = EnvironmentSplittingPath.of(EnvironmentParametersSplittingFactory.produce());
         state01 = StateGrid.of(0,1);
         state21 = StateGrid.of(2,1);
         state42 = StateGrid.of(4,2);
         state40 = StateGrid.of(4,0);
      }

    @ParameterizedTest
    @CsvSource({
            "N,  0,1,0,false,false",   //a,s -> s',r,isPark, isTerminal
            "E,  1,1,0,false,false",
            "S,  0,1,0,false,false",
             //"W,  0,1,0,false,false"  not possible
    })
    void givenPos01_whenStepping_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ParamGridTestDecoder.of(arguments);
        var sr= environment.step(state01, decoder.action());
        assertStepReturn(decoder, sr);
    }

    @ParameterizedTest
    @CsvSource({
            "N,  2,2,0,false,false",   //a,s -> s',r,isPark, isTerminal
            "E,  2,1,0,false,false",
            "S,  2,0,0,false,false"
         //   "W,  1,1,0,false,false"   //not possible
    })
    void givenPos21_whenStepping_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ParamGridTestDecoder.of(arguments);
        var sr= environment.step(state21, decoder.action());
        assertStepReturn(decoder, sr);
    }


    @ParameterizedTest
    @CsvSource({
            "N,  4,2,0,false,false",   //a,s -> s',r,isPark, isTerminal
            "E,  5,2,1,false,true",
            "S,  4,2,0,false,false",
            //"W,  3,2,0,false,false"  not possible
    })
    void givenPos42_whenStepping_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ParamGridTestDecoder.of(arguments);
        var sr= environment.step(state42, decoder.action());
        System.out.println("sr = " + sr);
        assertStepReturn(decoder, sr);
    }


    @ParameterizedTest
    @CsvSource({
            "N,  4,0,0,false,false",   //a,s -> s',r,isPark, isTerminal
            "E,  5,0,0,false,true",
            "S,  4,0,0,false,false",
            //"W,  3,0,0,false,false"  not possible
    })
    void givenPos40_whenStepping_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ParamGridTestDecoder.of(arguments);
        var sr= environment.step(state40, decoder.action());
        System.out.println("sr = " + sr);
        assertStepReturn(decoder, sr);
    }


    private static void assertStepReturn(ParamGridTestDecoder decoder, StepReturnGrid sr) {
        assertEquals(decoder.sNext(), sr.sNext());
        assertEquals(decoder.reward(), sr.reward());
        assertEquals(decoder.isFail(), sr.isFail());
        assertEquals(decoder.isTerminal(), sr.isTerminal());
    }


}
