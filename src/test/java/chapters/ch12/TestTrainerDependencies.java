package chapters.ch12;

import chapters.ch12.factory.TrainerDependenciesFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

 class TestTrainerDependencies {

    @Test
     void testConstructorAndGetters() {
        var dependencies = TrainerDependenciesFactory.createForTest();
        assertNotNull(dependencies.agent());
        assertNotNull(dependencies.environment());
        assertNotNull(dependencies.trainerParameters());
        assertNotNull(dependencies.startStateSupplier());
    }


}
