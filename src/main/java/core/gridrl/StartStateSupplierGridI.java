package core.gridrl;


public interface StartStateSupplierGridI {

    /**
     * Returns the name of the environment for which this supplier provides start states.
     * @return the environment name
     */
    String environmentName();

    /**
     * Returns the start state for the environment.
     * @return the start state
     */
    StateGrid getStartState();
}
