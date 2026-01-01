package chapters.ch5._shared.helper;

import chapters.ch5.domain.environment.ExperienceMc;
import chapters.ch5.domain.environment.StateMcI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.IntStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExperiencesInfo {

    public static boolean isFirstVisit(StateMcI state,
                                       int listPosition,
                                       List<ExperienceMc> experiences) {
        int iFirst = IntStream.range(0, experiences.size())
                .filter(i -> experiences.get(i).state().equals(state))
                .findFirst()
                .orElse(0);
        return listPosition == iFirst;
    }

}
