package chapters.ch8.domain.environment.core;

public record NewStateResult(StateParking state, boolean isPark, boolean isDeparting) {
    public static NewStateResult of(StateParking state, boolean isPark, boolean isDeparted) {
        return new NewStateResult(state, isPark, isDeparted);
    }
}