package chapters.ch11;


import chapters.ch11.domain.agent.core.AgentLunar;
import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.core.StepReturnLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierRandomHeightZeroSpeed;
import chapters.ch11.domain.trainer.core.ExperienceLunar;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.multisteps.MultiStepResults;
import chapters.ch11.domain.trainer.multisteps.MultiStepResultsGenerator;
import chapters.ch11.domain.trainer.param.TrainerParameters;
import chapters.ch11.factory.LunarAgentParamsFactory;
import chapters.ch11.factory.LunarEnvParamsFactory;
import chapters.ch11.factory.TrainerParamsFactory;
import chapters.ch11.helper.RadialBasisAdapter;
import core.foundation.gadget.training.TrainData;
import core.foundation.gadget.training.TrainDataOld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.List;

/****
 *  rewards of experiences: 1, 0, 1, 1, 1  (exp 4 goes to 5, exp 5 is terminal)
 */

class TestMultiStepResultsGenerator {
    public static final int N_FITS = 100;
    public static final int N_EPOCHS = 20;
    public static final double TOL_CRITIC = 0.01;
    public static final double TOL = 1e-4;
    public static final double LEARNING_RATE_CRITIC = 0.1;

    MultiStepResultsGenerator generator;
    TrainerDependencies dependencies;
    List<ExperienceLunar> experiencesSpreadOnes;
    List<ExperienceLunar> experiencesOneBigMinusInEnd;

    @BeforeEach
    void init() {
        generator = createGenerator(4, 1.0);
        var exp0 = expNotFail(0, false);
        var exp1 = expNotFail(1, false);
        var exp1tTerm = expNotFail(1, true);
        experiencesSpreadOnes = List.of(exp1, exp0, exp1, exp1, exp1tTerm);
        experiencesOneBigMinusInEnd = List.of(exp0, exp0, exp0, exp0, exp0, expNotFail(-100, true));
    }

    private MultiStepResultsGenerator createGenerator(int stepHorizon, double gamma) {
        var ep = LunarEnvParamsFactory.produceDefault();
        var p = LunarAgentParamsFactory.newDefault(ep)
                .withLearningRateCritic(LEARNING_RATE_CRITIC)
                .withBatchSize(1).withNEpochs(N_EPOCHS);
        var trainerParameters = TrainerParamsFactory.of(stepHorizon, N_FITS).withGamma(gamma);
        dependencies = TrainerDependencies.builder()
                .agent(AgentLunar.zeroWeights(p, ep))
                .environment(EnvironmentLunar.createDefault())
                .trainerParameters(trainerParameters)
                .startStateSupplier(StartStateSupplierRandomHeightZeroSpeed.create(ep))
                .build();

        return MultiStepResultsGenerator.of(dependencies);
    }

    @Test
    void whenReadingCritic_thenZeroValue() {
        var agent = dependencies.agent();
        double val = agent.readCritic(StateLunar.zeroPosAndSpeed());
        Assertions.assertEquals(0, val);
    }

    @Test
    void whenGenerating_thenCorrectNofExp() {
        var multiStepResults = generator.generate(experiencesSpreadOnes);
        Assertions.assertEquals(5, multiStepResults.nResults());
    }

    @ParameterizedTest   //step, value, advantage, isTerminalOrOutSide
    @CsvSource({
            "0, 1,1,false",
            "1, 0,0,false",
            "2, 1,1,false"
    })
    void givenStepHorizon1_whenGenerating_thenCorrectValueAndAdvantage(ArgumentsAccessor arguments) {
        generator = createGenerator(1, 1.0);
        assertMultiStepResults(ArgumentAdaptor.of(arguments), generator.generate(experiencesSpreadOnes));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 2,2,false",
            "1, 2,2,false",  //exp 1+3=4 is not terminalOrOutside
            "2, 3,3,true"    //exp 2+3=5 is terminalOrOutside
    })
    void givenStepHorizon3_whenGenerating_thenCorrectValueAndAdvantage(ArgumentsAccessor arguments) {
        generator = createGenerator(3, 1.0);
        assertMultiStepResults(ArgumentAdaptor.of(arguments), generator.generate(experiencesSpreadOnes));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 4,4,true",  //exp 0+5=5 is terminalOrOutside
            "1, 3,3,true",  //exp 1+5=6 is terminalOrOutside
            "2, 3,3,true"   //exp 2+5=7 is terminalOrOutside
    })
    void givenStepHorizon5_whenGenerating_thenCorrectValueAndAdvantage(ArgumentsAccessor arguments) {
        generator = createGenerator(5, 1.0);
        assertMultiStepResults(ArgumentAdaptor.of(arguments), generator.generate(experiencesSpreadOnes));
   }

    @Test
    void whenReadingFittedCritic_thenOneValue() {
        var agent = dependencies.agent();
        double vTar = 1.0;
        fitMemory(agent, vTar);
        double val = agent.readCritic(StateLunar.zeroPosAndSpeed());
        Assertions.assertEquals(vTar, val, TOL_CRITIC);
    }

  //rewards of experiences: 1, 0, 1, 1, 1  (exp 4 goes to 5, exp 5 is terminal)

    @ParameterizedTest   //step, value, isTerminalOrOutside advantage=returnMinusBase+value(sFut)-value(s)=value-value(s)
    @CsvSource({
            "0, 3,2,false",  //returnMinusBase=2, value(sFut) = 1 => value=3,  adv=3-1=2
            "1, 3,2,false",  //returnMinusBase=2, value(sFut) = 1 => value=3,  adv=3-1=2
            "2, 3,2,true",   //returnMinusBase=3, value(sFut) = 0 => value=3,  adv=3-1=2
            "4, 1,0,true"   //returnMinusBase=1, value(sFut) = 0 => value=1,  adv=1-1=0
    })
    void givenStepHorizon3AndCriticValue1_whenGenerating_thenCorrectValueAndAdvantage(ArgumentsAccessor arguments) {
        generator = createGenerator(3, 1.0);
        fitMemory(dependencies.agent(), 1.0);
        assertMultiStepResults(ArgumentAdaptor.of(arguments), generator.generate(experiencesSpreadOnes));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 1.25,1.25,false",  //returnMinusBase=1+0+.25, value(sFut) = 0 => value=1.25,  adv=1.5-0
            "1, 0.75,0.75,false",   //returnMinusBase=0+0.5+0.25, value(sFut) = 0 => value=0.75,  adv=0.75-1
    })
    void givenStepHorizon3AndCriticValue0AndGamma0dot5_whenGenerating_thenCorrectValueAndAdvantage(ArgumentsAccessor arguments) {
        double gamma = 0.5;
        int stepHorizon = 3;
        generator = createGenerator(stepHorizon, gamma);
        assertMultiStepResults(ArgumentAdaptor.of(arguments), generator.generate(experiencesSpreadOnes));
    }


    // 0,0,0,0,0,-100

    @ParameterizedTest
    @CsvSource({
            "0, 0.125,-0.875,false",  //returnMinusBase=0, value(sFut) = 0.5^3*1=1/8 => value=0.125,  adv=0.125-1=-0.875
            "2, 0.125,-0.875,false",   //returnMinusBase=0+0.5^3*0=0, value(sFut) = 0.5^3*1=1/8 => value=0.125,  adv=-0.875
            "3, -25,-26,true",   //returnMinusBase=0+0.5^2*-100=-25, value(sFut) = 0 => value=-25,  adv=-26
    })
    void givenOneBigMinusInEndExperience_whenGenerating_thenCorrectValueAndAdvantage(ArgumentsAccessor arguments) {
        double gamma = 0.5;
        int stepHorizon = 3;
        generator = createGenerator(stepHorizon, gamma);
        double vTarget = 1.0;
        fitMemory(dependencies.agent(), vTarget);
        assertMultiStepResults(ArgumentAdaptor.of(arguments), generator.generate(experiencesOneBigMinusInEnd));
    }


    record ArgumentAdaptor(int step, double value, double advantage, boolean isFutOutSide) {
        public static ArgumentAdaptor of(ArgumentsAccessor arguments) {
            return new ArgumentAdaptor(
                    arguments.getInteger(0),
                    arguments.getDouble(1),
                    arguments.getDouble(2),
                    arguments.getBoolean(3)
            );
        }
    }


    private static void assertMultiStepResults(ArgumentAdaptor aa, MultiStepResults msr) {
        Assertions.assertEquals(aa.value, msr.valueTarAtStep(aa.step),TOL);
        Assertions.assertEquals(aa.advantage, msr.advantageAtStep(aa.step),TOL);
        Assertions.assertEquals(aa.isFutOutSide, msr.isFutureOutsideOrTerminalAtStep(aa.step));
    }


    private static ExperienceLunar expNotFail(int reward, boolean isTerminal) {
        StateLunar state = StateLunar.zeroPosAndSpeed();
        StateLunar stateNew = StateLunar.zeroPosAndSpeed();
        return ExperienceLunar.of(
                state, 0d, StepReturnLunar.ofNotFail(stateNew, isTerminal, reward));
    }

    private void fitMemory(AgentLunar agent, double vTarget) {
        StateLunar state = StateLunar.zeroPosAndSpeed();
        var data= TrainData.empty();
        var in = RadialBasisAdapter.asInput(state);
        for (int i = 0; i < N_FITS ; i++) {
            data.clear();
            double err = vTarget - agent.readCritic(state);
            data.addListIn(in, err);
            agent.fitCritic(data);
        }

    }

}
