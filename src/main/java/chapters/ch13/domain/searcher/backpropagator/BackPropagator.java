package chapters.ch13.domain.searcher.backpropagator;

import k_mcts.domain.searcher.path.Path;
import k_mcts.domain.searcher.settings.SearcherSettings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * BackPropagator is responsible for updating the nodes in the search tree
 * based on the result of a simulation and traversed nodes.
 *
 * @param <S> The type of the state
 * @param <A> The type of the action
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BackPropagator<S, A> {

    SearcherSettings searcherSettings;

    public static <S, A> BackPropagator<S, A> of(SearcherSettings searcherSettings) {
        return new BackPropagator<>(searcherSettings);
    }

    /**
     * Updates the nodes in the search tree based on the result of a simulation.
     *
     * @param path The path that was simulated
     * @return True if the simulation ended in a fail, false otherwise
     */
    public boolean update(Path<S, A> path) {
        boolean isDefensiveBackup = path.info().isAnyFail();
        double discount = getDiscount(isDefensiveBackup);
        var returns = path.info().returns(discount);
        double learningRate = getLearningRate(isDefensiveBackup);
        var nodes = path.info().nodes();
        for (var node : nodes) {
            node.update(learningRate, returns.get(nodes.indexOf(node)));
        }
        return isDefensiveBackup;
    }

    private double getLearningRate(boolean isDefensiveBackup) {
        return isDefensiveBackup
                ? searcherSettings.learningRateDefensive()
                : searcherSettings.learningRateNormal();
    }

    private double getDiscount(boolean isDefensiveBackup) {
        return isDefensiveBackup
                ? searcherSettings.discountDefensive()
                : searcherSettings.discountNormal();
    }

}
