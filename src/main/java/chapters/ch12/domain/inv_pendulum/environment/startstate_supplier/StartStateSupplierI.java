package chapters.ch12.domain.inv_pendulum.environment.startstate_supplier;


import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;

public interface StartStateSupplierI {
    StatePendulum getStartState();
}
