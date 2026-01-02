package chapters.ch10;

import chapters.ch10.cannon.factory.FactoryTrainerParametersCannon;
import chapters.ch10.cannon.gradient_calculator.GradLogCalculatorContinuousAction;
import core.foundation.gadget.math.MeanAndStd;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

class TestGradLogCalculatorContinuousAction {

    GradLogCalculatorContinuousAction calculator;

     @BeforeEach
      void init() {
       var trainerParameters= FactoryTrainerParametersCannon.forTest();
       calculator=GradLogCalculatorContinuousAction.of(trainerParameters);
      }

    @ParameterizedTest
    @CsvSource({
            "0,0,1  ,0,-1",  //a,m,s,  gradzm, gradzs
            "1,0,1  ,1,0",
            "4,2,2.72  ,0.27,-0.46",   //worked ex 10.9
            "2,2,2.72  ,0,-1.0",
            "0,2,2.72  ,-0.27,-0.46"
    })  //
    void whenSum_thenCorrect1(ArgumentsAccessor arguments) {
        double a  = arguments.getDouble(0);
        double m  = arguments.getDouble(1);
        double s  = arguments.getDouble(2);
        double gradzm  = arguments.getDouble(3);
        double gradzs  = arguments.getDouble(4);

        var gradLog=calculator.gradLog(a, MeanAndStd.of(m,s));
        Assertions.assertEquals(gradzm,gradLog.mean(),1e-3);
        Assertions.assertEquals(gradzs,gradLog.std(),1e-3);
    }


}
