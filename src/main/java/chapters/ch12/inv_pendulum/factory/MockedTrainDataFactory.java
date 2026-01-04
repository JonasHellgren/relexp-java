package chapters.ch12.inv_pendulum.factory;

import chapters.ch12.inv_pendulum.domain.agent.memory.ActionAndItsValue;
import chapters.ch12.inv_pendulum.domain.environment.core.ActionPendulum;
import chapters.ch12.inv_pendulum.domain.environment.param.PendulumParameters;
import chapters.ch12.inv_pendulum.domain.environment.startstate_supplier.StartStateSupplierEnum;
import lombok.experimental.UtilityClass;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MockedTrainDataFactory {

    public static List<List<Double>> getListOfInputs() {
        List<List<Double>> listOfLists = new ArrayList<>();
        listOfLists.add(List.of(0.01, 0.02));
        listOfLists.add(List.of(-0.5, 0.1));
        listOfLists.add(List.of(0.3, 0.2));
        listOfLists.add(List.of(-1.0, -2.0));
        listOfLists.add(List.of(0.0, 0.0));
        listOfLists.add(List.of(1.0, 0.0));
        listOfLists.add(List.of(-1d, 0.0));
        listOfLists.add(List.of(0.001, 0.002));
        return listOfLists;
    }

    public static List<ActionAndItsValue> getActionValueList(List<List<Double>> listOfInputs) {
        double epsilon = 0.1;
        List<ActionAndItsValue> avList = new ArrayList<>();
        for (List<Double> in : listOfInputs) {
            Double angle = in.get(0);
            Double angSpd = in.get(1);
            if (Math.abs(angle) < epsilon && Math.abs(angSpd) < epsilon) {
                avList.add(ActionAndItsValue.of(ActionPendulum.N, 10.0));
            } else if (angle < 0) {
                avList.add(ActionAndItsValue.of(ActionPendulum.CW, 10.0));
            } else if (angle > 0) {
                avList.add(ActionAndItsValue.of(ActionPendulum.CCW, 10.0));
            }
        }
        return avList;
    }


    public static List<ActionAndItsValue> getActionValueListNegValues(
            List<List<Double>> listOfInputs) {
        double epsilon = 0.1;
        List<ActionAndItsValue> avList = new ArrayList<>();
        for (List<Double> in : listOfInputs) {
            Double angle = in.get(0);
            Double angSpd = in.get(1);
            if (Math.abs(angle) < epsilon && Math.abs(angSpd) < epsilon) {
                avList.add(ActionAndItsValue.of(ActionPendulum.N, 0.0));
            } else if (angle < 0) {
                avList.add(ActionAndItsValue.of(ActionPendulum.CCW, -10.0));
            } else if (angle > 0) {
                avList.add(ActionAndItsValue.of(ActionPendulum.CW, -10.0));
            }
        }
        return avList;
    }


    public static List<List<Double>> getListOfRandomInputs(int nInputs, PendulumParameters pp) {
        List<List<Double>> listOfLists = new ArrayList<>();
        var startStateSupplier= StartStateSupplierEnum.RANDOM_FEASIBLE_ANGLE_AND_SPEED.of(pp);
        for (int i = 0; i < nInputs; i++) {
            listOfLists.add(startStateSupplier.getStartState().asList());
        }
        return listOfLists;
    }


    public static List<ActionAndItsValue> getActionValueListWithValue(
            List<List<Double>> listOfInputs, double actionValue) {
        List<ActionAndItsValue> avList = new ArrayList<>();
        for (List<Double> in : listOfInputs) {
            avList.add(ActionAndItsValue.of(ActionPendulum.random(), actionValue));
        }
        return avList;
    }


}
