package chapters.ch14.domain.action_selector;

import java.util.List;

public interface ActionSelectorI<A> {
     List<A> selectActions(int depth);
}
