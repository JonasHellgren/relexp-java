package chapters.ch12.inv_pendulum.factory;

import chapters.ch12.inv_pendulum.domain.agent.core.AgentPendulum;
import chapters.ch12.inv_pendulum.domain.environment.core.EnvironmentPendulum;
import chapters.ch12.inv_pendulum.domain.environment.startstate_supplier.StartStateSupplierEnum;
import chapters.ch12.inv_pendulum.domain.environment.startstate_supplier.StartStateSupplierI;
import chapters.ch12.inv_pendulum.domain.trainer.core.TrainerDependencies;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TrainerDependenciesFactory {

    public static TrainerDependencies createForTest() {
        var agentParameters = AgentParametersFactory.createForTest();
        var trainerParameters = TrainerParametersFactory.createForTest();
        var agent = AgentPendulum.of(agentParameters, trainerParameters);
        var pendulumParameters = PendulumParametersFactory.createForTest();
        var environment = EnvironmentPendulum.of(pendulumParameters);
        var startStateSupplier = StartStateSupplierEnum
                .RANDOM_SMALL_ANGLE_ZERO_SPEED.create();
        return TrainerDependencies.of(
                agent, environment, trainerParameters, startStateSupplier);

    }

    public static TrainerDependencies createForTrainerTest() {
        var agentParameters = AgentParametersFactory.createForTrainerTest();
        var trainerParameters = TrainerParametersFactory.createForTrainerTest();
        var agent = AgentPendulum.of(agentParameters, trainerParameters);
        var pendulumParameters = PendulumParametersFactory.createForTrainerTest();
        var environment = EnvironmentPendulum.of(pendulumParameters);
        var startStateSupplier = StartStateSupplierEnum
                .RANDOM_SMALL_ANGLE_ZERO_SPEED.create();
        return TrainerDependencies.of(
                agent, environment, trainerParameters, startStateSupplier);
    }

    public static TrainerDependencies createForTrainerRunning(HyperParametersPendulum hp) {
        var agentParameters = AgentParametersFactory.createForTrainerRunning(
                hp.nHiddenLayers(),hp.nHiddenUnits());
        var trainerPar = TrainerParametersFactory.createForTrainerRunning(
                hp.nEpisodes(), hp.learningRateStartEnd(), hp.sizeMiniBatch());
        var agent = AgentPendulum.of(agentParameters, trainerPar);
        var pendulumParameters = hp.isFailPenalty()
                ? PendulumParametersFactory.createForTrainerRunningFailPenalty()
                : PendulumParametersFactory.createForTrainerRunningCloserToRefReward();
        var environment = EnvironmentPendulum.of(pendulumParameters);
        StartStateSupplierI ssSup = hp.isRandomStart()
                ? StartStateSupplierEnum.RANDOM_FEASIBLE_ANGLE_AND_SPEED.of(pendulumParameters)
                : StartStateSupplierEnum.SMALL_ANGLE_ZERO_SPEED.create();
        return TrainerDependencies.of(
                agent, environment, trainerPar, ssSup);
    }


}
