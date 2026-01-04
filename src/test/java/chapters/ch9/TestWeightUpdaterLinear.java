package chapters.ch9;

import chapters.ch9.gradient_descent.PhiExtractor;
import chapters.ch9.gradient_descent.WeightUpdaterLinear;
import core.foundation.gadget.training.TrainDataOld;
import core.foundation.gadget.training.Weights;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class TestWeightUpdaterLinear {

    public static final double LEARNING_RATE = 0.9;
    public static final int N_OF_WEIGHTS = 2;
    WeightUpdaterLinear updater;
    TrainDataOld data;

     @BeforeEach
      void init() {
         var phiExtractor = PhiExtractor.empty();
         phiExtractor.functionList.add(x -> 1);
         phiExtractor.functionList.add(x -> x.get(0));
         updater= WeightUpdaterLinear.of(LEARNING_RATE, phiExtractor);
         data= TrainDataOld.emptyFromOutputs();
         data.addIAndOut(List.of(0.1), 5.0+1.0);
         data.addIAndOut(List.of(0.3), 5.0+3.0);
         data.addIAndOut(List.of(0.5), 5.0+5.0);
         data.addIAndOut(List.of(0.8), 5.0+8.0);
         data.addIAndOut(List.of(1.0), 5.0+10.0);
     }

      @Test
       void givenData_whenUpdate_thenCorrectWeights() {
         var weights= Weights.allZero(N_OF_WEIGHTS);
          for (int i = 0; i < 100; i++) {
              updater.updateWeights(data,weights);
          }
          Assertions.assertEquals(5.0, weights.get(0),0.1);
          Assertions.assertEquals(10.0, weights.get(1),0.1);
      }

}
