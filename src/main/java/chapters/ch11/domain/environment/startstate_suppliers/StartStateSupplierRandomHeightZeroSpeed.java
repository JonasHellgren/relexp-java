package chapters.ch11.domain.environment.startstate_suppliers;

import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StartStateSupplierRandomHeightZeroSpeed implements StartStateSupplierI   {

    LunarParameters ep;

    public static StartStateSupplierI create(LunarParameters ep) {
        return new StartStateSupplierRandomHeightZeroSpeed(ep);
    }

    @Override
    public StateLunar getStartState() {
        return StateLunar.randomPosAndZeroSpeed(ep);
    }
}
