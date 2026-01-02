package chapters.ch10.bandit.domain.environment;

public record EnvironmentParametersBandit(double probCoinLeftArm, double rewardCoin) {

    public static EnvironmentParametersBandit of(double probCoinLeftArm, double rewardCoin) {
        return new EnvironmentParametersBandit(probCoinLeftArm,rewardCoin);
    }

    public double probCoinRightArm() {
        return 1 - probCoinLeftArm;
    }

}
