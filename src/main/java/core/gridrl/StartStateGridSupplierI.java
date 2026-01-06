package core.gridrl;


/**
 * Interface for a supplier of start states in a grid environment.
 * Implementations of this interface should provide a way to get the start state
 * for a specific environment.
 */
public interface StartStateGridSupplierI {

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
