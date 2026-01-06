package core.gridrl;


import chapters.ch4.domain.param.InformerGridParamsI;

/**
 * Interface for a grid environment.
 */
public interface EnvironmentGridI {
    /**
     * Returns the name of the environment.
     *
     * @return the name of the environment
     */
    String name();

    /**
     * Takes a step in the environment.
     *
     * @param state the current state of the environment
     * @param action the action to take in the environment
     * @return the return value of the step, including the new state, whether the
     * step resulted in a failure or termination, and the reward for the step
     */
    StepReturnGrid step(StateGrid state, ActionGrid action);

    /**
     * Returns the parameters of the environment.
     *
     * @return the parameters of the environment
     */
    //EnvironmentGridParametersI getParameters();


    InformerGridParamsI informer();
}
