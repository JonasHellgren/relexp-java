package chapters.ch10.bandit.domain.environment;

import lombok.Getter;

@Getter
public enum ActionBandit {
    LEFT(0), RIGHT(1);

    private final int index;

    ActionBandit(int index) {
        this.index = index;
    }

    public static ActionBandit fromIndex(int index) {
        for (ActionBandit action : values()) {
            if (action.index == index) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid index: " + index);
    }


}
