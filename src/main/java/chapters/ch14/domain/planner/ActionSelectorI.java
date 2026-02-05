package chapters.ch14.domain.planner;

import java.util.List;

public interface ActionSelectorI<A> {
     List<A> selectActions(int depth);
}
