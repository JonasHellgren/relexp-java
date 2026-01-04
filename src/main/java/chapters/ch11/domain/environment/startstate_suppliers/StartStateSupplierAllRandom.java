package chapters.ch11.domain.environment.startstate_suppliers;

import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StartStateSupplierAllRandom implements StartStateSupplierI   {

    LunarParameters ep;

    public static StartStateSupplierI create(LunarParameters ep) {
        return new StartStateSupplierAllRandom(ep);
    }

    @Override
    public StateLunar getStartState() {
        return StateLunar.randomPosAndSpeed(ep);
    }
}
