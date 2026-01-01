package chapters.ch5;

import chapters.ch5.domain.environment.ActionMcI;
import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.implem.splitting.ActionSplittingMc;
import chapters.ch5.implem.splitting.StateSplittingMc;
import chapters.ch5.implem.walk.ActionWalk;
import chapters.ch5.implem.walk.StateWalk;
import lombok.Builder;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;

@Builder
public record ParamMcTestDecoder(
        StateMcI state,
        ActionMcI action,
        StateMcI sNext,
        double reward,
        boolean isFail,
        boolean isTerminal
) {
    public static ParamMcTestDecoder ofWalk(ArgumentsAccessor arg) {
        return ParamMcTestDecoder.builder()
                .state(StateWalk.of(arg.getInteger(0)))
                .action(ActionWalk.valueOf(arg.getString(1)))
                .sNext(StateWalk.of(arg.getInteger(2)))
                .reward(arg.getDouble(3))
                .isFail(arg.getBoolean(4))
                .isTerminal(arg.getBoolean(5))
                .build();
    }

    public static ParamMcTestDecoder ofSplitting(ArgumentsAccessor arg) {
        return ParamMcTestDecoder.builder()
                .state(StateSplittingMc.of(arg.getInteger(0),arg.getInteger(1)))
                .action(ActionSplittingMc.valueOf(arg.getString(2)))
                .sNext(StateSplittingMc.of(arg.getInteger(3),arg.getInteger(4)))
                .reward(arg.getDouble(5))
                .isFail(arg.getBoolean(6))
                .isTerminal(arg.getBoolean(7))
                .build();
    }
    
}
