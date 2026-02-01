package chapters.ch13.implem.jumper;

import lombok.Builder;

import java.util.List;

@Builder
public record JumperParameters(
        int maxHeight,
        int maxPos,
        double rewardCoin,
        double rewardNotCoin,
        double rewardFail,
        List<StateJumper> posWithCoins
) {

}
