package chapters.ch12.plotting_bandit;

import chapters.ch12.domain.bandit.trainer.BanditActionValueMemory;
import lombok.Builder;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;

@Builder
public record MeasuresBanditNeural(
        double error,
        double valueLeft,
        double valueRight)
 {

  public static MeasuresBanditNeural getMeasures(BanditActionValueMemory memory,
                                                 DataSet dataSet,
                                                 INDArray output) {
   return MeasuresBanditNeural.builder()
           .error(memory.getModel().score(dataSet))
           .valueLeft(output.getDouble(0))
           .valueRight(output.getDouble(1))
           .build();
  }

 }
