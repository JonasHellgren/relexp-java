package chapters.ch7;

import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.Builder;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;

@Builder
public record TestArgumentsDecoderSafe(
        StateGrid state,
        ActionGrid action,
        boolean isCorrected
) {
    public static TestArgumentsDecoderSafe of(ArgumentsAccessor arg) {
        return TestArgumentsDecoderSafe.builder()
                .state(StateGrid.of(arg.getInteger(0), arg.getInteger(1)))
                .action(ActionGrid.valueOf(arg.getString(2)))
                .isCorrected(arg.getBoolean(3))
                .build();
    }
}
