package chapters.ch10.bandit.domain.environment;

public record StepReturnBandit(double reward, boolean isCoin) {

    public static StepReturnBandit of(double reward, boolean isCoin) {
        return new StepReturnBandit(reward, isCoin);
    }

}
