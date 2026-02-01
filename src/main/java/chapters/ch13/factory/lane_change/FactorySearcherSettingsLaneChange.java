package chapters.ch13.factory.lane_change;

import chapters.ch13.domain.searcher.core.SearcherParameters;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactorySearcherSettingsLaneChange {


    public static SearcherParameters test() {
        return SearcherParameters.builder()
                .uctExploration(10.0)  //2  or 10
                .maxTreeDepth(3*4+4)  //4 steps per sec, max 3 sec
                .maxDepth(500).maxIterations(50_000)
                .discountNormal(1.0).learningRateNormal(1.0)
                //.discountNormal(1.0).learningRateNormal(0.1)
                .discountDefensive(0.5).learningRateDefensive(0.1)
                //.discountDefensive(0.5).learningRateDefensive(0.01)
                .build();
    }





}
