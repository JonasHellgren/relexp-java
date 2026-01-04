package chapters.ch14.domain.trainer;

import chapters.ch14.domain.environment.Experience;
import core.foundation.gadget.training.TrainData;
import java.util.List;

public interface MiniBatchAdapterI<SI,S,A> {
    TrainData adapt(List<Experience<S,A>> experiences);
}
