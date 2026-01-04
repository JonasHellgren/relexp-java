package chapters.ch12.inv_pendulum.domain.environment.startstate_supplier;


import chapters.ch12.inv_pendulum.domain.environment.core.StatePendulum;

public interface StartStateSupplierI {
    StatePendulum getStartState();
}
