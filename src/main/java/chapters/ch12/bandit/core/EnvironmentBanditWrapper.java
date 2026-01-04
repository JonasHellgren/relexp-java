package chapters.ch12.bandit.core;

import chapters.ch10.bandit.domain.environment.ActionBandit;
import chapters.ch10.bandit.domain.environment.EnvironmentBandit;
import chapters.ch10.bandit.domain.environment.EnvironmentParametersBandit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentBanditWrapper {

    EnvironmentBandit environment;

    public static EnvironmentBanditWrapper of(EnvironmentParametersBandit params) {
        return new EnvironmentBanditWrapper(EnvironmentBandit.of(params));
    }
    public double step(int  action) {
        var actionBandit= ActionBandit.fromIndex(action);
        var sr= environment.step(actionBandit);
        return sr.reward();
    }
}
