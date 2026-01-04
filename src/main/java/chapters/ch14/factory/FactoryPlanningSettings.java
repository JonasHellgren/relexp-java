package chapters.ch14.factory;

import chapters.ch14.domain.settings.PlanningSettings;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryPlanningSettings {

    public static PlanningSettings forTest() {
        return PlanningSettings.builder()
                .maxTimeInMs(100)
                .minNofIterations(100)
                .maxDepth(15)
                .probActionChange(0.3)
                .build();
    }

    public static PlanningSettings forRunning() {
        return PlanningSettings.builder()
                .maxTimeInMs(10)
                .minNofIterations(100)
                .maxDepth(10)
                .probActionChange(0.3)
                .build();
    }


    public static PlanningSettings forExecutor(int maxDepth) {
        return forTest().withMaxDepth(maxDepth);
    }
}
