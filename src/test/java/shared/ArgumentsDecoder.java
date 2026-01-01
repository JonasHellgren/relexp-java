package shared;

import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.Builder;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;

@Builder
public record ArgumentsDecoder(
        StateGrid state,
        ActionGrid action,
        double value
) {
    public static ArgumentsDecoder of(ArgumentsAccessor arg) {
        return ArgumentsDecoder.builder()
                .state(StateGrid.of(arg.getInteger(0), arg.getInteger(1)))
                .action(ActionGrid.valueOf(arg.getString(2)))
                .value(arg.getDouble(3))
                .build();
    }
}
