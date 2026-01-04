package chapters.ch11.domain.environment.core;


import chapters.ch11.domain.environment.param.LunarParameters;

public interface EnvironmentI {
    StepReturnLunar step(StateLunar state, double action);
    LunarParameters getParameters();
}
