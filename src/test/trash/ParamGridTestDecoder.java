package chapters.ch2;

import lombok.Builder;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;

@Builder
public record ParamGridTestDecoder(
        ActionGrid action,
        StateGrid sNext,
        double reward,
        boolean isFail,
        boolean isTerminal
) {
    public static ParamGridTestDecoder of(ArgumentsAccessor arg) {
        return ParamGridTestDecoder.builder()
                .action(ActionGrid.valueOf(arg.getString(0)))
                .sNext(StateGrid.of(arg.getInteger(1), arg.getInteger(2)))
                .reward(arg.getDouble(3))
                .isFail(arg.getBoolean(4)).isTerminal(arg.getBoolean(5))
                .build();
    }
}
