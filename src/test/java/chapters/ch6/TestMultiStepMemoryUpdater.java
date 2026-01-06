package chapters.ch6;

import core.gridrl.ExperienceGrid;
import chapters.ch6.domain.trainer.mutlisteps_during_epis.MultiStepMemoryUpdater;
import chapters.ch6.implem.factory.TrainerDependenciesFactory;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import core.gridrl.StepReturnGrid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class TestMultiStepMemoryUpdater {

    public static final int N_EPISODES = 200;
    public static final double TOL = 0.3;
    public static final double LEARNING_RATE_START = 0.1;
    public static final double REWARD = 1.0;
    public static final double GAMMA_LOW = 0.5;
    public static final double LEARNING_RATE_ONE = 1.0;
    public static final ActionGrid ACTION_IN_EXPERIENCE = ActionGrid.E;
    public static final int N_STEPS_HORIZON_3 = 3;
    MultiStepMemoryUpdater memoryUpdater1, memoryUpdater3;
    MultiStepMemoryUpdater memoryUpdater1lowGamma, memoryUpdater3lowGamma;
    List<ExperienceGrid> experiences;

    @BeforeEach
    void init() {
        var dependencies1 = TrainerDependenciesFactory.givenOptimalPolicySplitting(1, N_EPISODES, LEARNING_RATE_START);
        var dependencies3 = TrainerDependenciesFactory.givenOptimalPolicySplitting(N_STEPS_HORIZON_3, N_EPISODES, LEARNING_RATE_START);
        memoryUpdater1 = MultiStepMemoryUpdater.of(dependencies1);
        memoryUpdater3 = MultiStepMemoryUpdater.of(dependencies3);
        memoryUpdater1lowGamma = MultiStepMemoryUpdater.of(dependencies1.withTrainerParameters
                (dependencies1.trainerParameters().withGamma(GAMMA_LOW)));
        memoryUpdater3lowGamma = MultiStepMemoryUpdater.of(dependencies3.withTrainerParameters
                (dependencies3.trainerParameters().withGamma(GAMMA_LOW)));
        experiences = getExperiences();
    }


    @Test
    void testExperiences() {
        Assertions.assertEquals(6, experiences.size());
        Assertions.assertEquals(1, experiences.get(0).reward());
    }

    @Test
    void testNonUpdatedAgentMemory() {
        var agent = memoryUpdater1.getDependencies().agent();
        Assertions.assertEquals(0, agent.read(StateGrid.of(0, 1), ActionGrid.E));
        Assertions.assertEquals(0, agent.read(StateGrid.of(0, 1), ActionGrid.N));
        Assertions.assertEquals(0, agent.read(StateGrid.of(1, 1), ActionGrid.W));
    }

    @Test
    void givenUpdater1_thenCorrect() {
        var updater = memoryUpdater1;
        var agent = updater.getDependencies().agent();
        int tau = 1;
        updater.updateAgentMemory(tau, experiences, LEARNING_RATE_ONE);
        tau = 0;
        updater.updateAgentMemory(tau, experiences, LEARNING_RATE_ONE);

        Assertions.assertEquals(1.0, agent.read(StateGrid.of(1, 1), ACTION_IN_EXPERIENCE));  //tao=1
        Assertions.assertEquals(2.0, agent.read(StateGrid.of(0, 1), ACTION_IN_EXPERIENCE));  //tao=0, backing up from (1,1)
    }

    @Test
    void givenUpdater3_thenCorrect() {
        var updater = memoryUpdater3;
        var agent = updater.getDependencies().agent();
        int tau = N_STEPS_HORIZON_3;
        updater.updateAgentMemory(tau, experiences, LEARNING_RATE_ONE);
        tau = 0;
        updater.updateAgentMemory(tau, experiences, LEARNING_RATE_ONE);
        tau = 5;
        updater.updateAgentMemory(tau, experiences, LEARNING_RATE_ONE);

        Assertions.assertEquals(3.0, agent.read(StateGrid.of(N_STEPS_HORIZON_3, 1), ACTION_IN_EXPERIENCE));  //tao=3
        Assertions.assertEquals(6.0, agent.read(StateGrid.of(0, 1), ACTION_IN_EXPERIENCE));  //tao=0, backing up from (3,1)
        Assertions.assertEquals(1.0, agent.read(StateGrid.of(5, 1), ACTION_IN_EXPERIENCE));  //tao=5, truncated
    }

    @Test
    void givenUpdater1lowGamma_thenCorrect() {
        var updater = memoryUpdater1lowGamma;
        var agent = updater.getDependencies().agent();
        int tau = 1;
        updater.updateAgentMemory(tau, experiences, LEARNING_RATE_ONE);
        tau = 0;
        updater.updateAgentMemory(tau, experiences, LEARNING_RATE_ONE);

        Assertions.assertEquals(1, agent.read(StateGrid.of(1, 1), ACTION_IN_EXPERIENCE));  //tao=3
        Assertions.assertEquals(1.5, agent.read(StateGrid.of(0, 1), ACTION_IN_EXPERIENCE));  //tao=0, backing up from (3,1)
    }


    @Test
    void givenUpdater3lowGamma_thenCorrect() {
        var updater = memoryUpdater3lowGamma;
        var agent = updater.getDependencies().agent();
        int tau = N_STEPS_HORIZON_3;
        updater.updateAgentMemory(tau, experiences, LEARNING_RATE_ONE);
        tau = 0;
        updater.updateAgentMemory(tau, experiences, LEARNING_RATE_ONE);

        double sum = 1d + 1d / 2 + 1d / 4;
        Assertions.assertEquals(sum, agent.read(StateGrid.of(N_STEPS_HORIZON_3, 1), ACTION_IN_EXPERIENCE));  //tao=3
        double gammaPow3 = Math.pow(GAMMA_LOW, N_STEPS_HORIZON_3);
        Assertions.assertEquals(sum + gammaPow3 * sum, agent.read(StateGrid.of(0, 1), ACTION_IN_EXPERIENCE));  //tao=0, backing up from (3,1)
    }

    private static List<ExperienceGrid> getExperiences() {
        return List.of(
                getExperienceGrid(0),
                getExperienceGrid(1),
                getExperienceGrid(2),
                getExperienceGrid(N_STEPS_HORIZON_3),
                getExperienceGrid(4),
                getExperienceGridTerminal(5));
    }

    private static ExperienceGrid getExperienceGrid(int x) {
        return getExperienceGrid(x, false);
    }

    private static ExperienceGrid getExperienceGridTerminal(int x) {
        return getExperienceGrid(x, true);
    }

    private static ExperienceGrid getExperienceGrid(int x, boolean isTerminal) {
        return ExperienceGrid.ofSarsa(
                StateGrid.of(x, 1),
                ACTION_IN_EXPERIENCE,
                StepReturnGrid.of(StateGrid.of(x + 1, 1), false, isTerminal, REWARD),
                ACTION_IN_EXPERIENCE);
    }

}
