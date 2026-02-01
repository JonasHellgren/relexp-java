package chapters.ch13.factory.jumper;

import chapters.ch13.implem.jumper.JumperParameters;
import chapters.ch13.implem.jumper.StateJumper;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class JumperParametersFactory {


    /**
     * List of positions with coins.
     */
    static final List<StateJumper> POS_WITH_COINS = List.of(
            StateJumper.of(1, 1),
            StateJumper.of(2, 2),
            StateJumper.of(3, 3));

    public static JumperParameters produce() {
        return JumperParameters.builder()
                .maxHeight(3)
                .maxPos(3)
                .rewardCoin(1.0)
                .rewardNotCoin(0.0)
                .rewardFail(-10.0)
                .posWithCoins(POS_WITH_COINS)
                .build();
    }
}
