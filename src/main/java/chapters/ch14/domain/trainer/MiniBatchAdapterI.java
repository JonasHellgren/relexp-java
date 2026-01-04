package chapters.ch14.domain.trainer;

import chapters.ch14.domain.environment.Experience;
import core.foundation.gadget.training.TrainDataOld;
import java.util.List;

public interface MiniBatchAdapterI<SI,S,A> {
    TrainDataOld adapt(List<Experience<S,A>> experiences);
}
