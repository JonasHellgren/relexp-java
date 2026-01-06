package chapters.ch7.factory;

import core.gridrl.StateActionGrid;
import core.gridrl.TrainerGridDependencies;
import chapters.ch7.domain.safety_layer.SafetyLayer;
import core.gridrl.ActionGrid;
import lombok.experimental.UtilityClass;

/**
 * A utility class for creating safety layers for the treasure hunt environment.
 * Hard codes fail actions
 */

@UtilityClass
public class SafetyLayerFactoryTreasure {

    public static SafetyLayer produce(TrainerGridDependencies dependencies) {
        var safetyLayer = SafetyLayer.empty(dependencies);
        safetyLayer.add(StateActionGrid.of(1, 1, ActionGrid.S));
        safetyLayer.add(StateActionGrid.of(1, 1, ActionGrid.N));
        safetyLayer.add(StateActionGrid.of(2, 0, ActionGrid.W));
        safetyLayer.add(StateActionGrid.of(2, 2, ActionGrid.W));
        safetyLayer.add(StateActionGrid.of(6, 3, ActionGrid.E));
        safetyLayer.add(StateActionGrid.of(5, 1, ActionGrid.S));
        safetyLayer.add(StateActionGrid.of(6, 1, ActionGrid.S));
        safetyLayer.add(StateActionGrid.of(7, 1, ActionGrid.S));
        safetyLayer.add(StateActionGrid.of(8, 1, ActionGrid.S));
        return safetyLayer;
    }
}
