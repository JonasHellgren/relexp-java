package chapters.ch13.factory.jumper;

public record RunnerSettings(
        double uctExploration,
        double discountDefensive,
        double learningRateDefensive
) {

    public static RunnerSettings of(double uctExploration,
                                    double discountDefensive,
                                    double learningRateDefensive) {
        return new RunnerSettings(uctExploration,discountDefensive,learningRateDefensive);
    }

}

