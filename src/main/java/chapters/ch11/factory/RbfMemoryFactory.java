package chapters.ch11.factory;

import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch9.radial_basis_old.Kernel;
import chapters.ch9.radial_basis_old.Kernels;
import chapters.ch9.radial_basis_old.RbfNetwork;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RbfMemoryFactory {

    public static RbfNetwork createMemoryManyCenters(AgentParameters p,
                                                     LunarParameters ep,
                                                     double learningRate) {
        var kernels= Kernels.empty();
        for (double yNorm : ep.ySpace(p.nKernelsY())) {
            for (double spdNorm : ep.spdSpace(p.nKernelsSpeed())) {
                var kernel= Kernel.ofGammas(new double[]{yNorm, spdNorm}, p.gammas());
                kernels.addKernel(kernel);
            }
        }
        return RbfNetwork.of(kernels,learningRate);
    }

}
